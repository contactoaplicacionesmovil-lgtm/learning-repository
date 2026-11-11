package com.example.marsphotos.data

interface MarsPhotosRepository {
    suspend fun getMarsPhotos(): List<MarsPhoto>
}

class NetworkMarsPhotosRepository(private val marsApiService: MarsApiService) :
    MarsPhotosRepository {

    override suspend fun getMarsPhotos(): List<MarsPhoto>  = marsApiService.getPhotos()

}