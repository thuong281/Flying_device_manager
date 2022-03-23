package com.example.flyingdevicemanager.app_ui.user_profile.review_user_kyc.paging

import androidx.paging.*
import com.example.flyingdevicemanager.api.Api
import com.example.flyingdevicemanager.models.User
import retrofit2.HttpException
import java.io.IOException

class UserPagingSource(private val api: Api, private val token: String) :
    PagingSource<Int, User>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: 1
        return try {
            val response = api.getAllUserKyc(token, position)
            val listUsers = response.body()?.data ?: ArrayList()
            
            LoadResult.Page(
                data = listUsers,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (listUsers.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: NullPointerException) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}