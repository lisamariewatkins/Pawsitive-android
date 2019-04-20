package com.example.petmatcher.DI

/**
 * This empty interface is a flag we look for when adding activities and fragments in [ApplicationInjector]. It is meant only
 * for Android components, so Activities, Fragments, Services, or Broadcast Receivers.
 *
 * If the class implements this interface, we will call [AndroidInjector.inject] on this class when it is created.
 */
interface Injectable