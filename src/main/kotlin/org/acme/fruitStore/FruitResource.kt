package org.acme.fruitStore

import org.jboss.logging.Logger
import org.jboss.resteasy.annotations.jaxrs.PathParam
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.json.Json
import javax.persistence.EntityManager
import javax.transaction.Transactional
import javax.ws.rs.*
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider


@Path("fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
class FruitResource {
    @Inject
    var entityManager: EntityManager? = null

    @GET
    fun get(): Array<Fruit> {
        return entityManager!!.createNamedQuery("Fruits.findAll", Fruit::class.java)
                .resultList.toTypedArray()
    }

    @GET
    @Path("{id}")
    fun getSingle(@PathParam id: Int): Fruit {
        return entityManager!!.find(Fruit::class.java, id)
                ?: throw WebApplicationException("Fruit with id of $id does not exist.", 404)
    }

    @POST
    @Transactional
    fun create(fruit: Fruit): Response {
        if (fruit.id != null) {
            throw WebApplicationException("Id was invalidly set on request.", 422)
        }
        entityManager!!.persist(fruit)
        return Response.ok(fruit).status(201).build()
    }

    @PUT
    @Path("{id}")
    @Transactional
    fun update(@PathParam id: Int, fruit: Fruit): Fruit {
        if (fruit.name == null) {
            throw WebApplicationException("Fruit Name was not set on request.", 422)
        }
        val entity = entityManager!!.find(Fruit::class.java, id)
                ?: throw WebApplicationException("Fruit with id of $id does not exist.", 404)
        entity.name = fruit.name
        return entity
    }

    @DELETE
    @Path("{id}")
    @Transactional
    fun delete(@PathParam id: Int): Response {
        val entity = entityManager!!.getReference(Fruit::class.java, id)
                ?: throw WebApplicationException("Fruit with id of $id does not exist.", 404)
        entityManager!!.remove(entity)
        return Response.status(204).build()
    }

    @Provider
    class ErrorMapper : ExceptionMapper<Exception> {
        override fun toResponse(exception: Exception): Response {
            LOGGER.error("Failed to handle request", exception)
            var code = 500
            if (exception is WebApplicationException) {
                code = exception.response.status
            }
            val entityBuilder = Json.createObjectBuilder()
                    .add("exceptionType", exception.javaClass.name)
                    .add("code", code)
            if (exception.message != null) {
                entityBuilder.add("error", exception.message)
            }
            return Response.status(code)
                    .entity(entityBuilder.build())
                    .build()
        }
    }

    companion object {
        private val LOGGER = Logger.getLogger(FruitResource::class.java.name)
    }
}