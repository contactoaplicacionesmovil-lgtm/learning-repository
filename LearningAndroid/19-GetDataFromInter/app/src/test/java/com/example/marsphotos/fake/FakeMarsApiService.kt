package com.example.marsphotos.fake

import com.example.marsphotos.data.MarsApiService
import com.example.marsphotos.data.MarsPhoto

class FakeMarsApiService : MarsApiService {

    override suspend fun getPhotos(): List<MarsPhoto> {
        return FakeDataSource.photosList
    }
}