<template>
  <div>
    <div class="button-container">
      <button @click="filterGoouts(null)">전체</button>
      <button @click="filterGoouts(0)">기안중</button>
      <button @click="filterGoouts(1)">진행중</button>
      <button @click="filterGoouts(3)">반려</button>
      <button @click="filterGoouts(2)">결재 완료</button>
      <button @click="filterGoouts(4)">등록 취소</button>
    </div>
    <div class="gooutList">
      <table>
        <thead>
          <tr>
            <th>게시글 번호</th>
            <th>이름</th>
            <th>작성자 이름</th>
            <th>휴가 유형</th>
            <th>상태</th>
            <th>시작 날짜</th>
            <th>종료 날짜</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="goout in filteredGoouts"
            :key="goout.id"
            @click="goToGooutReadPage(goout.id)"
            class="gooutItem"
          >
            <td style="text-align: center">{{ goout.id }}</td>
            <td>{{ goout.name }}</td>
            <td>{{ goout.writerName }}</td>
            <td>{{ goout.gooutTypeName }}</td>
            <td>{{ getStatusText(goout.status) }}</td>
            <td>{{ goout.first }}</td>
            <td>{{ goout.last }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="pagination">
      <button @click="prevGroup">이전</button>
      <button
        v-for="page in pageGroup"
        :key="page"
        :class="{ active: page === currentPage }"
        @click="changePage(page)"
      >
        {{ page }}
      </button>
      <button @click="nextGroup">이후</button>
    </div>

    <div class="button-container2">
      <button @click="goToGooutCreate">휴가 등록</button>
    </div>
  </div>
</template>
<script>
import axios from "axios";
export default {
  name: "VacationPage",
  data() {
    return {
      goouts: [],
      filteredGoouts: [],
      currentPage: 1,
      pageSize: 10,
      pagesToShow: 5,
      pageGroupStart: 1, // 페이지 그룹의 시작 페이지 번호
      totalPages: 0, // 전체 페이지 수
    };
  },
  created() {
    this.fetchGoouts();
  },
  computed: {
    pageGroup() {
      // 현재 페이지가 포함된 페이지 그룹의 시작 페이지를 계산합니다.
      let startPage =
        Math.floor((this.currentPage - 1) / this.pagesToShow) *
          this.pagesToShow +
        1;
      // 시작 페이지를 기준으로 pagesToShow만큼의 페이지 번호를 생성합니다.
      // 단, 전체 페이지 수를 초과하지 않도록 주의합니다.
      let pages = [];
      for (let i = 0; i < this.pagesToShow; i++) {
        let page = startPage + i;
        if (page > this.totalPages) break; // 전체 페이지 수를 초과하지 않도록 합니다.
        pages.push(page);
      }
      return pages;
    },
  },
  methods: {
    changePage(page) {
      // 페이지를 변경하고, 새로운 페이지의 데이터를 불러옵니다.
      this.currentPage = page;
      this.fetchGoouts();
    },

    prevGroup() {
      // 이전 그룹으로 이동 (페이지 번호 배열만 -5)
      this.pageGroupStart = Math.max(1, this.pageGroupStart - this.pagesToShow);
      // 현재 페이지도 페이지 그룹의 첫 페이지로 설정
      this.changePage(this.pageGroupStart);
    },
    nextGroup() {
      // 다음 그룹으로 이동 (페이지 번호 배열만 +5)
      if (this.pageGroupStart + this.pagesToShow <= this.totalPages) {
        this.pageGroupStart += this.pagesToShow;
        // 현재 페이지도 페이지 그룹의 첫 페이지로 설정
        this.changePage(this.pageGroupStart);
      }
    },

    getStatusText(status) {
      const statusMap = {
        0: "대기중",
        1: "기안중",
        2: "최종 승인",
        3: "반려",
        4: "등록 취소",
        // 필요한 다른 상태들...
      };
      return statusMap[status] || "알 수 없음";
    },
    goToGooutCreate() {
      this.$router.push("/goout/create");
    },
    async fetchGoouts() {
      try {
        const response = await axios.get(`http://192.168.0.51/api/goout/check`, {
          params: {
            page: this.currentPage,
            size: this.pageSize,
          },
        });
        const data = response.data.result; // 결과 데이터 구조에 따라 조정 필요
        this.goouts = data.goouts;
        this.filteredGoouts = data.goouts;
            // 총 페이지 수 계산
    this.totalPages = Math.ceil(data.totalElements / this.pageSize);
      } catch (error) {
        console.error("Failed to fetch goouts:", error);
      }
    },
    goToGooutReadPage(id) {
      if (id) {
        this.$router.push(`/goout/read/${id}`);
      } else {
        console.error("ID is undefined");
      }
    },
    filterGoouts(status) {
      if (status === null) {
        this.filteredGoouts = this.goouts;
      } else {
        this.filteredGoouts = this.goouts.filter(
          (goout) => goout.status === status
        );
      }
    },
  },
};
</script>
<style scoped>
.active {
  font-weight: bold;
  color: red;
}
.button-container {
  text-align: left;
  padding-right: 40px;
  margin-bottom: 20px;
}
.button-container2 {
  text-align: right;
  padding-right: 40px;
  margin-bottom: 20px;
}
.button-container button,
.button-container2 button {
  font-size: 18px;
  font-weight: 600;
  padding: 5px 10px;
  color: white; /* 글씨 색상을 흰색으로 설정 */
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111; /* 기본 배경색을 검정색으로 설정 */
  margin-left: 10px; /* 버튼 간격 조정 */
  transition: background-color 0.3s; /* 부드러운 색상 전환 효과 */
}
.button-container button:hover,
.button-container2 button:hover {
  background-color: #f75c29; /* 마우스를 올렸을 때 배경색을 주황색으로 변경 */
  color: white;
}
.gooutList ul {
  list-style: none;
  padding: 0;
}
.gooutItem {
  cursor: pointer;
  margin: 10px 0;
  transition: color 0.3s ease;
}

.gooutList table {
  width: 100%;
  border-collapse: collapse;
}
.gooutList th,
.gooutList td {
  border: 1px solid #ddd;
  padding: 8px;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px; /* 페이지네이션과 위의 내용 사이에 공간 추가 */
}
</style>
