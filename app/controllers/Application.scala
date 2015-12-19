package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import org.hashids._


// http://stackoverflow.com/questions/30543960/play-framework-for-scala-compilation-errortype-application-is-not-a-member-of
class Application extends Controller {

  val pageForm = Form(
      "url" -> nonEmptyText
    )

  def index = Action { request =>
    Ok(views.html.index(Page.all(), pageForm, request.host))
  }

  def newUrl = Action { request =>
    Ok(views.html.new_url(pageForm))
  }

  def handlePostUrl = Action { implicit request =>
    pageForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Page.all(), errors, request.host)),
      url => {
        var new_url:String = url
        if(new_url.startsWith("http://"))
          new_url = new_url
        else if (new_url.startsWith("https://"))
          new_url = new_url
        else if (new_url.startsWith("//"))
          new_url = "http://" + new_url.stripPrefix("//")
        else
          new_url = "http://" + new_url
        Page.create(new_url)
        Redirect(routes.Application.index)
        })
  }

  def short(shortened:String) = Action { request =>
    val ip = request.remoteAddress
    val hashids = Hashids.reference("some secret here", 3)
    val page_id = hashids.decode(shortened).head
    val page = Page.find(page_id)
    Page.increment(page)
    var visit:Visit = Visit.find.where().eq("ip", ip).conjunction().eq("page", page).endJunction().findUnique()
    if (visit==null) {
      visit = Visit.create(ip, page)
    }

    Visit.increment(visit)
    Redirect(page.url)
  }

  def visits(page_id:Int) = Action { request =>
    val page = Page.find(page_id)
    Ok(views.html.page(page, Visit.filter(page), request.remoteAddress))
  }

}
