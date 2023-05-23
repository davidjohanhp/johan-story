package com.example.proyekstory.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.proyekstory.data.StoryRepository
import com.example.proyekstory.model.Story
import com.example.proyekstory.ui.home.StoryAdapter
import com.example.proyekstory.utils.DataDummy
import com.example.proyekstory.utils.MainDispatcherRule
import com.example.proyekstory.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock private lateinit var storyRepository: StoryRepository

    private val dummyStoriesResponse = DataDummy.generateDummyStories()
    private val dummyNullStoriesResponse = ArrayList<Story>()

    @Test
    fun `when Get Stories Should Not Null and Return Success`() = runTest {
        val data: PagingData<Story> = StoryPagingSource.snapshot(dummyStoriesResponse)
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedStories)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStories: PagingData<Story> = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStoriesResponse, differ.snapshot())
        assertEquals(dummyStoriesResponse.size, differ.snapshot().size)
        assertEquals(dummyStoriesResponse[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Stories Should Null`() = runTest {
        val data: PagingData<Story> = StoryPagingSource.snapshot(ArrayList<Story>())
        val expectedStories = MutableLiveData<PagingData<Story>>()
        expectedStories.value = data
        Mockito.`when`(storyRepository.getStory()).thenReturn(expectedStories)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStories: PagingData<Story> = mainViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStories)

        assertNotNull(differ.snapshot())
        assertEquals(dummyNullStoriesResponse, differ.snapshot())
        assertEquals(dummyNullStoriesResponse.size, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}