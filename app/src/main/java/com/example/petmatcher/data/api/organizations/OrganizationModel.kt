package com.example.petmatcher.data.api.organizations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class OrganizationJsonResponse(@field:Json(name = "organizations") val organizations: List<Organization>,
                                    @field:Json(name = "pagination") val pagination: Pagination)

/**
 * @author Lisa Watkins
 *
 * Entity that represents an organizations table in the database.
 */
// TODO: Figure out how to add the list of photo urls to database
@Entity(tableName = "organizations")
data class Organization(
    @PrimaryKey
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "email") val email: String?,
    @field:Json(name = "phone") val phone: String?,
    @Embedded
    @field:Json(name = "address") val address: Address?,
    @Embedded
    @field:Json(name = "hours") val hours: Hours?,
    @field:Json(name = "url") val url: String?,
    @Embedded
    @field:Json(name = "social_media") val socialMedia: SocialMedia?,
    @field:Json(name = "website") val website: String?,
    @field:Json(name = "mission_statement") val missionStatement: String?)


data class Hours(@field:Json(name = "monday") val monday: String?,
                  @field:Json(name = "tuesday") val tuesday: String?,
                  @field:Json(name = "wednesday") val wednesday: String?,
                  @field:Json(name = "thursday") val thursday: String?,
                  @field:Json(name = "friday") val friday: String?,
                  @field:Json(name = "saturday") val saturday: String?,
                  @field:Json(name = "sunday") val sunday: String?)

data class Adoption(@field:Json(name = "policy") val primary: String?,
                  @field:Json(name = "url") val secondary: String?)

data class SocialMedia(@field:Json(name = "facebook") val facebook: String?,
                      @field:Json(name = "twitter") val twitter: String?,
                      @field:Json(name = "youtube") val youtube: String?,
                      @field:Json(name = "instagram") val instagram: String?,
                      @field:Json(name = "pinterest") val pinterest: String?)

data class Photo(@field:Json(name = "small") val small: String,
                 @field:Json(name = "medium") val medium: String,
                 @field:Json(name = "large") val large: String,
                 @field:Json(name = "full") val full: String)

data class Address(@field:Json(name = "address1") val address1: String?,
                   @field:Json(name = "address2") val address2: String?,
                   @field:Json(name = "city") val city: String?,
                   @field:Json(name = "state") val state: String?,
                   @field:Json(name = "postcode") val postcode: String?,
                   @field:Json(name = "country") val country: String?)

data class Pagination(@field:Json(name = "count_per_page") val countPerPage: Int,
                      @field:Json(name = "total_count") val totalCount: Int,
                      @field:Json(name = "current_page") val currentPage: Int,
                      @field:Json(name = "total_pages") val totalPages: Int,
                      @field:Json(name = "_links") val links: Links)

data class Links(@field:Json(name = "previous") val previous: Link,
                 @field:Json(name = "next") val next: Link)

data class Link(@field:Json(name = "href") val href: String)
