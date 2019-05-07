package com.example.petmatcher.data

import com.example.database.api.organizations.*
import com.example.models.*
import com.example.models.organization.*
import com.example.models.organization.Link
import com.example.models.organization.Links
import com.example.petmatcher.data.api.organizations.*

fun generateMockOrganizationJsonResponse(): com.example.models.organization.OrganizationJsonResponse {
    return com.example.models.organization.OrganizationJsonResponse(
        generateListOfMockOrganizations(10),
        com.example.models.organization.Pagination(
            countPerPage = 5,
            totalCount = 5,
            currentPage = 1,
            totalPages = 10,
            links = com.example.models.organization.Links(
                previous = com.example.models.organization.Link("previous"),
                next = com.example.models.organization.Link("next")
            )
        )
    )
}

fun generateListOfMockOrganizations(size: Int): List<com.example.models.organization.Organization> {
    return (1..size).map {
        com.example.models.organization.Organization(
            id = "id",
            missionStatement = "mission statement",
            name = "name",
            phone = "Phone",
            socialMedia = com.example.models.organization.SocialMedia(
                facebook = "Facebook",
                twitter = "Twitter",
                instagram = "Instagram",
                pinterest = "Pinterest",
                youtube = "Youtube"
            ),
            email = "email",
            hours = com.example.models.organization.Hours(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
            ),
            address = com.example.models.organization.Address(
                "address1",
                "address2",
                "city",
                "state",
                "postcode",
                "country"
            ),
            url = "url",
            website = "website"
        )
    }
}