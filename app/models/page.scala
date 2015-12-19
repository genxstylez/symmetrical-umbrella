package models

import com.avaje.ebean.Ebean
import javax.persistence._
import play.db.ebean._
import play.data.validation.Constraints._
import scala.collection.JavaConverters._
import controllers._
import org.hashids._


/**
 * Dao for a given Entity bean type.
 */
abstract class Dao[T](cls:Class[T]) {

  /**
   * Find by Id.
   */
  def find(id:Any):T = {
    return Ebean.find(cls, id)
  }

  /**
   * Find with expressions and joins etc.
   */
  def find():com.avaje.ebean.Query[T] = {
    return Ebean.find(cls)
  }

  /**
   * Return a reference.
   */
  def ref(id:Any):T = {
    return Ebean.getReference(cls, id)
  }


  /**
   * Save (insert or update).
   */
  def save(o:Any):Unit = {
    Ebean.save(o)
  }

  /**
   * Delete.ß
   */
  def delete(o:Any):Unit = {
    Ebean.delete(o)
  }
}

@Entity
@Table(name="Page")
class Page {
    @Id
    var id:Int = 0

    @Column(unique=true)
    var url:String = null

    @Column(unique=true)
    var short_url:String = null

    var visit_count:Int = 0

    @OneToMany(mappedBy="page")
    var visits:List[Visit] = null

}

object Page extends Dao(classOf[Page]) {

    def all() : List[Page] = Page.find.findList().asScala.toList

    def create(url:String) {
        var page = new Page
        page.url = url
        Page.save(page)
        val hashids = Hashids.reference("some secret here", 3)
        var shortened = hashids.encode(page.id)
        page.short_url = shortened
        Page.save(page)
    }

    def increment(page:Page) {
        page.visit_count += 1
        Page.save(page)

    }

}


@Entity
@Table(name="Visit")
class Visit {
    @Id
    var id:Int = 0

    var ip:String = null

    var count:Int = 0

    @ManyToOne
    var page:Page = null
}

object Visit extends Dao(classOf[Visit]) {
    def all(): List[Visit] = Visit.find.findList().asScala.toList

    def filter(page:Page): List[Visit] = {
        return Visit.find.where().eq("page", page).findList().asScala.toList
    }

    def create(ip:String, page:Page): Visit = {
        var visit = new Visit
        visit.ip = ip
        visit.page = page
        Visit.save(visit)
        return visit
    }

    def increment(visit:Visit) {
        visit.count += 1
        Visit.save(visit)
    }
}