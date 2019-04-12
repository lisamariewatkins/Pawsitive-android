package com.example.petmatcher.data

import com.example.petmatcher.testextensions.InstantExecutorExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(InstantExecutorExtension::class)
class OrganizationRepositoryTest {
    @Nested
    inner class getShelters {
        @Test
        fun `verify method returns result`() = runBlocking {

        }
    }

}