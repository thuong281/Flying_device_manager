package com.example.flyingdevicemanager.app_ui.add_device.history.paging

import androidx.paging.*
import com.example.flyingdevicemanager.api.Api
import com.example.flyingdevicemanager.models.Device2
import retrofit2.HttpException
import java.io.IOException

class DevicePagingSource(private val api: Api, private val token: String) : PagingSource<Int, Device2>() {
    
    override fun getRefreshKey(state: PagingState<Int, Device2>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Device2> {
        val position = params.key ?: 1
        return try {
            val response = api.getListAddDeviceHistory(token, position)
            val listAddDevices = response.body()?.data ?: ArrayList()
        
            LoadResult.Page(
                data = listAddDevices,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (listAddDevices.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: NullPointerException) {
            LoadResult.Error(e)
        }
    }
    
}