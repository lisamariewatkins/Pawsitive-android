package com.example.petmatcher.data

import com.example.database.api.organizations.*
import com.example.network.organizations.*
import com.example.petmatcher.data.api.organizations.*

fun generateMockOrganizationJsonResponse(): OrganizationJsonResponse {
    return OrganizationJsonResponse(
        generateListOfMockOrganizations(10),
        Pagination(
            countPerPage = 5,
            totalCount = 5,
            currentPage = 1,
            totalPages = 10,
            links = Links(
                previous = Link("previous"),
                next = Link("next")
            )
        )
    )
}

fun generateListOfMockOrganizations(size: Int): List<Organization> {
    return (1..size).map {
        Organization(
            id = "id",
            missionStatement = "mission statement",
            name = "name",
            phone = "Phone",
            socialMedia = SocialMedia(
                facebook = "Facebook",
                twitter = "Twitter",
                instagram = "Instagram",
                pinterest = "Pinterest",
                youtube = "Youtube"
            ),
            email = "email",
            hours = Hours(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
            ),
            address = Address(
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