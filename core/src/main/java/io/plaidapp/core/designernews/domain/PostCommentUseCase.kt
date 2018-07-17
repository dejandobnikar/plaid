/*
 * Copyright 2018 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.plaidapp.core.designernews.domain

import io.plaidapp.core.data.CoroutinesContextProvider
import io.plaidapp.core.data.Result
import io.plaidapp.core.designernews.data.comments.CommentsRepository
import io.plaidapp.core.designernews.data.comments.model.CommentResponse
import io.plaidapp.core.designernews.data.login.LoginRepository
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class PostCommentUseCase(
    private val commentsRepository: CommentsRepository,
    private val loginRepository: LoginRepository,
    private val contextProvider: CoroutinesContextProvider
) {
    /**
     * Post a comment to a story.
     */
    fun postStoryComment(
        body: String,
        storyId: Long,
        onResult: (result: Result<CommentResponse>) -> Unit
    ) = launch(contextProvider.io) {
        checkNotNull(loginRepository.user) {
            "User should be logged in, in order to post a comment"
        }
        val userId = loginRepository.user!!.id
        val result = commentsRepository.postStoryComment(
            body,
            storyId,
            userId
        )
        withContext(contextProvider.main) { onResult(result) }
    }
}