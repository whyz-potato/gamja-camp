<template>
  <div>
    <v-card flat>
      <v-card-title>{{ post.title }}</v-card-title>
      <v-divider></v-divider>
      <div class="mt-5">
        <div>
          <v-img v-for="img in post.images" :key="img.index" 
          :src="img" width="300"></v-img>
          <div>
            {{ post.content }}
          </div>
        </div>
        <v-divider></v-divider>
        <div class="comment">
          <div class="comment-list" ref="ref">
            <v-list v-for="comment in comments" :key="comment.index">
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>{{comment.content}}</v-list-item-title>
                </v-list-item-content>
                
                <span class="caption">{{comment.writer}}</span>
                <v-list-item-avatar>
                  <v-img :src="require(`@/assets/imgs/루피.jpg`)"></v-img>
                </v-list-item-avatar>
                <span class="caption" @click="updateComment(comment.id, comment.content)">수정</span>
                <span class="caption" @click="deleteComment(comment.id)">삭제</span>
                <span class="caption" @click="commentReply(comment.id, comment.content, comment.writer)">답글</span>
              </v-list-item>

              <v-list-item v-for="reply in comment.lowerComments" :key="reply.index"
                class="ml-10">
                <v-list-item-content>
                  <v-list-item-title>{{reply.content}}</v-list-item-title>
                </v-list-item-content>
                <span class="caption">{{reply.writer}}</span>
                <v-list-item-avatar>
                  <v-img :src="require(`@/assets/imgs/루피.jpg`)"></v-img>
                </v-list-item-avatar>
              </v-list-item>
            </v-list>
          </div>
          
          <div class="mt-10 d-flex">
            <v-text-field v-model="comment" :placeholder="inputLabel" solo rounded></v-text-field>
            <v-btn @click="commentRegister">등록</v-btn>
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
      comment: null,
      comments: [],
      inputLabel: '댓글쓰기',
      commentId: null,
      bottom: true,
      upperComment: null
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
    commentRegister () {
      let comment = { content: this.comment }

      if (this.commentId) {
        api.put(`/post/general/comment/update/${this.commentId}`, comment).then(() => {
          console.log('수정완')
          this.bottom = false
          this.comment = null
          this.commentId = null
          this.inputLabel = '댓글쓰기'
          this.getPostComment()
        })
      } else if (this.upperComment) {
        let reply = { content: this.comment, upperCommentId: this.upperComment.id }
        api.post(`/post/general/comment/new/${this.postId}`, reply).then(() => {
          console.log('등록완')
          this.getPostComment()
          this.upperComment = null
          this.bottom = false
          this.comment = null
          this.inputLabel = '댓글쓰기'
        })
      } else {
        api.post(`/post/general/comment/new/${this.postId}`, comment, {
          headers: {
            'X-XSRF-TOKEN': this.token,
          }
        }).then(() => {
          console.log('등록완')
          this.bottom = true
          this.getPostComment()
          this.comment = null
        })
      }
    },
    updateComment (commentId, commentContent) {
      this.commentId = commentId
      this.comment = commentContent
      this.inputLabel = '댓글수정'
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
    commentReply (id, content, writer) {
      this.upperComment = { id: id, content: content, writer: writer }
      this.inputLabel = '답글쓰기'
    }
  }
  

}
</script>


<style scoped>
.comment {
  width: 80%;
  margin: auto;
  margin-top: 20px;
}
.comment-list {
  height: 300px;
  overflow-y: auto;
}
.comment-reply {
  height: 300px;
  padding: 12px;
}
</style>