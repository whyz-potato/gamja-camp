<template>
  <div>
    <v-card flat>
      <v-card-title class="border">
        <div class="border flex-fill">
          {{ post.title }}
        </div>
        <div class="border">
          <v-btn @click="updatePost" small icon color="black">수정</v-btn>
          <v-btn @click="deletePost" small icon color="black">삭제</v-btn>
        </div>
      </v-card-title>
      <v-divider></v-divider>
      <div class="mt-5">
        <div class="mx-5">
          <v-img v-for="img in post.images" :key="img.index" 
          :src="img" width="300"></v-img>
          <div class="my-10">
            {{ post.content }}
          </div>
        </div>
        <v-divider></v-divider>
        <div class="comment">
          <div class="comment-list" ref="ref">
            <v-list v-for="item in comments" :key="item.index">
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="item.writer.profile"></v-img>
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title>{{item.writer.username}}</v-list-item-title>
                  <v-list-item-subtitle class="mt-1">{{item.content}}</v-list-item-subtitle>
                  <v-list-item-subtitle class="caption mt-1">
                    {{item.date}} &#183; {{item.time.toString().slice(0,5)}}
                    <span class="reply-btn ml-3" @click="commentId = item.id, isReply = true">답글쓰기</span>
                  </v-list-item-subtitle>
                </v-list-item-content>

                <span class="btn caption" @click="commentId = item.id, comment = item.content">수정</span>
                <span class="btn caption ml-1" @click="deleteComment(item.id)">삭제</span>
              </v-list-item>

              <div v-if="item.id == commentId" class="mx-5 mt-5">
                <v-textarea v-model="comment" rows="3" auto-grow solo
                  @keyup.enter="commentRegister"></v-textarea>
                <div class="text-right mt-n4 mb-5">
                  <v-btn @click="commentId = null, comment = null" text rounded>취소</v-btn>
                  <v-btn @click="commentRegister" text rounded>저장하기</v-btn>
                </div>  
              </div>

              <v-divider></v-divider>
              <div>
                <v-list v-for="reply in item.lowerComments" :key="reply.index" class="grey lighten-5">
                  <v-list-item class="ml-10">
                    <v-list-item-avatar>
                      <v-img :src="reply.writer.profile"></v-img>
                    </v-list-item-avatar>

                    <v-list-item-content>
                      <v-list-item-title>{{reply.writer.username}}</v-list-item-title>
                      <v-list-item-subtitle class="mt-1">{{reply.content}}</v-list-item-subtitle>
                      <v-list-item-subtitle class="caption mt-1">
                        {{reply.date}} &#183; {{reply.time.toString().slice(0,5)}} 
                      </v-list-item-subtitle>
                    </v-list-item-content>

                    <span class="btn caption" @click="commentId = reply.id, comment = reply.content">수정</span>
                    <span class="btn caption ml-1" @click="deleteComment(reply.id)">삭제</span>          
                  </v-list-item>  

                  <div v-if="reply.id == commentId" class="ml-15 mr-5 mt-5">
                    <v-textarea v-model="comment" rows="3" auto-grow solo
                      @keyup.enter="commentRegister"></v-textarea>
                    <div class="text-right mt-n4 mb-5">
                      <v-btn @click="commentId = null, comment = null" text rounded>취소</v-btn>
                      <v-btn @click="commentRegister" text rounded>저장하기</v-btn>
                    </div>  
                  </div>

                </v-list>
              </div>
            </v-list>
          </div>
          
          <div class="mt-10">
            <v-textarea v-model="newComment" label="댓글쓰기" rows="4" 
              auto-grow solo @keyup.enter="newCommentRegister"></v-textarea>
            <div class="text-right mt-n4 mb-5">
              <v-btn @click="newCommentRegister" text rounded>저장하기</v-btn>
            </div>
            
          </div>
        </div>
      </div>

      
    </v-card>
  </div>
</template>


<script>
import api from '@/api'

export default {
  data () {
    return {
      token: null,
      post: {},
      postId: null,
      newComment: null,
      comment: null,
      comments: [],
      commentId: null,
      //bottom: true,
      isReply: false
    }
  },
  mounted () {
    api.get('/chats/csrf').then(res => {
      this.token = res.data.token
    })

    this.postId = this.$route.query.postId
    this.getPostDetail()
    this.getPostComment()
  },
  watch: {
    comments () {
      this.$nextTick(() => {
        if (this.bottom) {
          this.$refs.ref.scrollTo({ top: this.$refs.ref.scrollHeight, behavior: 'smooth' })
        }
      })
    }
  },
  methods: {
    getPostDetail () {
      api.get(`/post/general/${this.postId}`).then(res => {
        console.log(res.data)
        this.post = res.data
      })
    },
    getPostComment () {
      api.get(`/post/general/comment/list/${this.postId}`).then(res => {
        console.log(res.data)
        this.comments = res.data
      })
    },
    newCommentRegister () {
      let comment = { content: this.newComment }

      api.post(`/post/general/comment/new/${this.postId}`, comment, {
        headers: {
          'X-XSRF-TOKEN': this.token,
        }
      }).then(() => {
        console.log('등록완')
        this.bottom = true
        this.getPostComment()
        this.newComment = null
      })
    },
    commentRegister () {
      if (this.isReply) {
        let reply = { content: this.comment, upperCommentId: this.commentId }

        api.post(`/post/general/comment/new/${this.postId}`, reply).then(() => {
          console.log('답글등록완')
          this.getPostComment()
          this.bottom = false
          this.comment = null
          this.commentId = null
          this.isReply = false
        })
      } else {
        let comment = { content: this.comment }

        api.put(`/post/general/comment/update/${this.commentId}`, comment).then(() => {
          console.log('수정완')
          this.bottom = false
          this.comment = null
          this.commentId = null
          this.getPostComment()
        })
      }
    },
    deleteComment (commentId) {
      this.bottom = false

      api.delete(`/post/general/comment/delete/${commentId}`, {
        headers: {
          'X-XSRF-TOKEN': this.token,
        }
      }).then(() => {
        console.log('삭제완')
        this.getPostComment()
      })
    },
    updatePost () {
      this.$router.push({ name: 'GeneralUpdate', query: { 'postId': this.postId }})
    },
    deletePost () {
      api.delete(`/post/general/delete/${this.postId}`).then(() => {

        this.$router.push({ name: 'Community' })
      })
    }
  }
  

}
</script>


<style scoped>
.comment {
  width: 90%;
  margin: auto;
  margin-top: 20px;
}
/* .comment-list {
  max-height: 500px;
  overflow-y: auto;
} */
.comment-reply {
  height: 300px;
  padding: 12px;
}
.reply-btn {
  cursor: pointer;
}
.btn {
  cursor: pointer;
}
</style>