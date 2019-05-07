package com.example.models

import com.squareup.moshi.Json

data class AnimalJsonResponse(@field:Json(name = "animals") val animals: List<Animal>)

data class Animal(@field:Json(name = "id") val id: Int,
                  @field:Json(name = "organization_id") val organizationId: String,
                  @field:Json(name = "url") val url: String,
                  @field:Json(name = "type") val type: String,
                  @field:Json(name = "species") val species: String,
                  @field:Json(name = "breeds") val breeds: Breeds,
                  @field:Json(name = "colors") val colors: Colors,
                  @field:Json(name = "age") val age: String,
                  @field:Json(name = "gender") val gender: String,
                  @field:Json(name = "size") val size: String,
                  @field:Json(name = "coat") val coat: String?,
                  @field:Json(name = "attributes") val attributes: Attributes,
                  @field:Json(name = "environment") val environment: Environment,
                  @field:Json(name = "tags") val tags: List<String>,
                  @field:Json(name = "name") val name: String,
                  @field:Json(name = "description") val description: String,
                  @field:Json(name = "photos") val photos: List<com.example.models.organization.Photo>,
                  @field:Json(name = "status") val status: String,
                  @field:Json(name = "published_at") val publishedAt: String,
                  @field:Json(name = "contact") val contact: Contact,
                  @field:Json(name = "pagination") val pagination: com.example.models.organization.Pagination
)

data class Breeds(@field:Json(name = "primary") val primary: String,
                  @field:Json(name = "secondary") val secondary: String?,
                  @field:Json(name = "tertiary") val tertiary: String?)

data class Colors(@field:Json(name = "primary") val primary: String?,
                  @field:Json(name = "secondary") val secondary: String?,
                  @field:Json(name = "tertiary") val tertiary: String?)

data class Attributes(@field:Json(name = "spayed_neutered") val spayedNeutered: Boolean,
                      @field:Json(name = "house_trained") val house_trained: Boolean,
                      @field:Json(name = "declawed") val declawed: Boolean?,
                      @field:Json(name = "special_needs") val specialNeeds: Boolean,
                      @field:Json(name = "shots_current") val shots_current: Boolean)

data class Environment(@field:Json(name = "children") val children: Boolean?,
                       @field:Json(name = "dogs") val dogs: Boolean?,
                       @field:Json(name = "cats") val cats: Boolean?)

data class Photo(@field:Json(name = "small") val small: String,
                  @field:Json(name = "medium") val medium: String,
                  @field:Json(name = "large") val large: String,
                  @field:Json(name = "full") val full: String)

data class Contact(@field:Json(name = "email") val email: String,
                   @field:Json(name = "phone") val phone: String,
                   @field:Json(name = "address") val address: com.example.models.organization.Address
)

data class Address(@field:Json(name = "address1") val address1: String,
                   @field:Json(name = "address2") val address2: String,
                   @field:Json(name = "city") val city: String,
                   @field:Json(name = "state") val state: String,
                   @field:Json(name = "postcode") val postcode: String,
                   @field:Json(name = "country") val country: String)

data class Pagination(@field:Json(name = "count_per_page") val countPerPage: Int,
                      @field:Json(name = "total_count") val totalCount: Int,
                      @field:Json(name = "current_page") val currentPage: Int,
                      @field:Json(name = "total_pages") val totalPages: Int,
                      @field:Json(name = "_links") val links: com.example.models.organization.Links
)

data class Links(@field:Json(name = "previous") val previous: com.example.models.organization.Link,
                 @field:Json(name = "next") val next: com.example.models.organization.Link
)

data class Link(@field:Json(name = "href") val href: String)
