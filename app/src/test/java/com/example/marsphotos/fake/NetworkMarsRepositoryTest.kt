package com.example.marsphotos.fake

import com.example.marsphotos.data.NetworkMarsPhotosRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals

class NetworkMarsRepositoryTest {

    /*
     The coroutine test library provides the runTest() function. The function takes the method
     that you passed in the lambda and runs it from TestScope, which inherits from CoroutineScope.
     */
    @Test
    fun networkMarsPhotosRepository_getMarsPhotos_verifyPhotoList()= runTest{
        val repository = NetworkMarsPhotosRepository(
            ///we need a n object of FakeMarsApiService class
            marsApiService = FakeMarsApiService()
        )
        assertEquals(FakeDataSource.photosList, repository.getMarsPhotos())
    }
}