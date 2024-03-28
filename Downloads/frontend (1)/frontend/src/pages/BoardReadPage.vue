<template>
  <HeaderComponent />
  <SideBar />
  
  <div class="container">

      <!-- <HeaderComponent />
    <SideBar /> -->
    <h1>공지사항</h1>

    <div class="board-detail">
      <strong>제목</strong>
      <span>{{ boardTitle }}</span>
      <span2 style="margin-left: 420px;">{{ boardDate }}</span2>
    </div>

    <div class="board-detail">
      <strong>이름</strong>
      <span>{{ boardName }}</span>
    </div>

    <div class="board-detail">
      <strong>파일</strong>
      
      <a :href="`/board/read/${boardId}`">{{boardFileName.split('_')[1]}}</a>
    </div>

    <div class="board-detail">
      <strong></strong>
      <p>{{ boardText }}</p>
    </div>

    <hr>
  </div>
</template>

<script>
import axios from 'axios';
import SideBar from '../components/SideBar.vue';
import HeaderComponent from '../components/HeaderComponent.vue';

export default {
  name: 'BoardReadPage',
components: {
      SideBar,
    HeaderComponent,
  },
  data() {
    return {
      boardId: '', // 추가된 부분: 게시글의 id를 저장하기 위한 변수
      boardTitle: '',
      boardDate: '',
      boardName: '',
      boardFileName: '', // 파일 이름을 저장할 변수
      boardText: ''
    };
  },
  mounted() {
    this.boardId = this.$route.params.id; // 라우터로부터 게시글의 id를 받아옴

    axios.get(`http://localhost:8080/board/read?id=${this.boardId}`)
      .then(response => {
        const board = response.data;

        this.boardTitle = board.title;
        this.boardDate = board.date;
        this.boardName = board.name;
        this.boardFileName = board.filename; // 파일 이름 설정
        this.boardText = board.text;
      })
      .catch(error => {
        console.error('Error fetching board detail:', error);
      });
  }
};
</script>

<style>

p{
  margin-top: -37px;
  margin-bottom: 16px;
  padding: 41px
}
body {
  font-family: Arial, sans-serif;
  background-color: #ffffff;
  margin: 0;
  padding: 0;
}

.container {
  max-width: 970px;
  margin: 94px auto;
  padding: 16px;
  background-color: #fff;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0);
  border-radius: 5px;
  font-size: -25px;
  margin-top: 88px;
}

h1 {
  margin-bottom: 36px;
  text-align: center;
  font-size: 27px;
  margin-left: -738px;
}

.board-detail {
  margin-bottom: 18px;
  position: relative;
}

.board-detail strong {
  display: inline-block;
  margin-bottom: 5px;
  width: 95px;
}

.board-detail span {
  display: inline-block;
  margin-left: -4px;
}

.board-detail:not(:last-child)::after {
  content: '';
  display: block;
  width: 100%;
  height: 1px;
  background-color: #ccc;
  position: absolute;
  bottom: -3px;
  left: 0;
}

#boardContent {
  width: 90%;
}

#boardDate {
  float: right;
}

hr {
  margin-top: 20px;
  border: none;
  border-top: 0px solid #ccc;
  margin-top: -262px;
}
</style>
