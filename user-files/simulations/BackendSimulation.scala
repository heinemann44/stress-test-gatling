import scala.concurrent.duration._

import scala.util.Random

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class BackendSimulation
  extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:9999")

  def criacaoEConsultaPessoas(tenantId: String) = {
    scenario(tenantId +  " Criação E Talvez Consulta de Pessoas")
      .feed(tsv("pessoas-payloads.tsv").circular())
      .exec(
        http("criação")
          .post("/pessoas").body(StringBody("#{payload}"))
          .header("content-type", "application/json")
          .header("tenant-id", tenantId)
          .check(status.in(201, 422, 400))
          .check(status.saveAs("httpStatus"))
          .checkIf(session => session("httpStatus").as[String] == "201") {
            header("Location").saveAs("location")
          }
      )
      .pause(1.milliseconds, 30.milliseconds)
      .doIf(session => session.contains("location")) {
        exec(
          http("consulta")
            .get("#{location}")
            .header("tenant-id", tenantId)
        )
      }
  }

  def buscaPessoas(tenantId: String) = {
    scenario(tenantId + " Busca Válida de Pessoas")
      .feed(tsv("termos-busca.tsv").circular())
      .exec(
        http("busca válida")
          .get("/pessoas?t=#{t}")
          .header("tenant-id", tenantId)
          // qq resposta na faixa 2XX tá safe
      )
  }

  def buscaInvalidaPessoas(tenantId: String) = {
    scenario(tenantId + " Busca Inválida de Pessoas")
      .exec(
        http("busca inválida")
          .get("/pessoas")
          .header("tenant-id", tenantId)
          // 400 - bad request se não passar 't' como query string
        .check(status.is(400))
      )
  }

  def setUpTenant(tenant: String) = {
    List(
      criacaoEConsultaPessoas(tenant).inject(
        constantUsersPerSec(2).during(10.seconds), // warm up
        constantUsersPerSec(5).during(15.seconds).randomized, // are you ready?
        rampUsersPerSec(6).to(150).during(1.minutes) // lezzz go!!!
      ),
      buscaPessoas(tenant).inject(
        constantUsersPerSec(2).during(25.seconds), // warm up
        rampUsersPerSec(6).to(30).during(1.minutes) // lezzz go!!!
      ),
      buscaInvalidaPessoas(tenant).inject(
        constantUsersPerSec(2).during(25.seconds), // warm up
        rampUsersPerSec(6).to(10).during(1.minutes) // lezzz go!!!
      )
    )
  }

  setUp(
    (
      setUpTenant("tenant-1") 
      ++ setUpTenant("tenant-2") 
      ++ setUpTenant("tenant-3")
      ++ setUpTenant("tenant-4")
      ++ setUpTenant("tenant-5")
      ++ setUpTenant("tenant-6")
      ++ setUpTenant("tenant-7")
      ++ setUpTenant("tenant-8")
      ++ setUpTenant("tenant-9")
      ++ setUpTenant("tenant-10")
      ++ setUpTenant("tenant-11")
      ++ setUpTenant("tenant-12")
      ++ setUpTenant("tenant-13")
      ++ setUpTenant("tenant-14")
      ++ setUpTenant("tenant-15")
    ): _*
  ).protocols(httpProtocol)
}
