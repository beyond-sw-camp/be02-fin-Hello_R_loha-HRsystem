<template>
    <div>
      <HeaderComponent />
      <SideBar />
  
      <h1>게시글 작성</h1>
  
      <form @submit.prevent="submitForm" id="boardForm" class="container">
        <div class="section-divider"></div>
        <div class="flex-container">
          <label for="name">이름</label>
          <input type="text" id="name" name="name" required v-model="form.name" class="input-field">
        </div>
        <div class="section-divider"></div>
        <div class="flex-container">
          <label for="title">제목</label>
          <input type="text" id="title" name="title" required v-model="form.title" class="input-field">
        </div>
        <div class="section-divider"></div>
        <div class="flex-container">
          <label for="text">내용</label>
          <textarea v-model="form.text" id="text" name="text" rows="8" cols="50" required class="input-field"></textarea>
        </div>
        <div class="section-divider"></div>
        <div class="flex-container">
          <label for="image">이미지</label>
          <input type="file" id="image" name="image" accept="image/*" @change="handleImageUpload">
          <img v-if="imageUrl" :src="imageUrl" alt="Uploaded Image" style="max-width: 100px; max-height: 100px; margin-left: 10px;">
        </div>
        <div class="section-divider"></div>
        <input type="submit" value="작성" class="submit-button">
      </form>
  
      <br>
      <br>
  
      <p class="contact-info">
        추가적인 문의사항은 아래로 연락바랍니다.<br>
        <br>
        Phone: 010.0000.0000 <br>
        <br>
        Email: 000001234@gmail.com <br>
      </p>
  
      <!-- 공지사항 작성 완료 알림 -->
      <div v-if="showSuccessMessage" class="success-message">
        공지사항이 성공적으로 등록되었습니다.
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import SideBar from '../components/SideBar.vue';
  import HeaderComponent from '../components/HeaderComponent.vue';
  
  export default {
    name: 'BoardCreatePage',
    components: {
      SideBar,
      HeaderComponent,
    },
    data() {
      return {
        form: {
          name: '',
          title: '',
          text: '',
          filename: '', // 수정: 이미지 파일 이름을 저장할 필드 추가
        },
        imageUrl: '',
        showSuccessMessage: false,
      };
    },
    methods: {
      async submitForm() {
        try {
          const formData = new FormData();
          formData.append('name', this.form.name); 
          formData.append('title', this.form.title);
          formData.append('text', this.form.text);
          
  
          const response = await axios.post("http://localhost:8080/board/create", formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });
          console.log("Response:", response.data);
          this.showSuccessMessage = true;
          setTimeout(() => {
            this.showSuccessMessage = false;
            // 작성 완료 후 페이지 리디렉션
            this.$router.push('/board/list');
          }, 3000);
        } catch (error) {
          console.error("Error:", error);
        }
      },
      handleImageUpload(event) {
        const file = event.target.files[0];
        if (file) {
          this.form.filename = file.name; // 수정: 이미지 파일 이름 설정
          this.imageUrl = URL.createObjectURL(file);
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .container {
    max-width: 970px;
    margin: auto;
    padding: 16px;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 5px;
  }
  
  h1 {
    text-align: center;
    margin-top: 70px;
  }
  
  .input-field {
    width: calc(100% - 130px);
    padding: 8px;
    margin-bottom: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
  }
  
  .section-divider {
    border-bottom: 1px solid #ccc;
    margin-bottom: 20px;
    padding-bottom: 10px;
    font-weight: bold;
  }
  
  .submit-button {
    background-color: #1a1817;
    color: white;
    padding: 10px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    float: right;
    margin-top: 20px;
  }
  
  .submit-button:hover {
    background-color: #ef7b2d;
  }
  
  .contact-info {
    text-align: center;
    font-size: 14px;
  }
  
  /* 공지사항 작성 완료 알림 스타일 */
  .success-message {
    background-color: #4CAF50;
    color: white;
    text-align: center;
    padding: 10px;
    margin-top: 10px;
    border-radius: 5px;
  }
  </style>
  