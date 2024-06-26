<template>
  <HeaderComponent />
  <SideBar />

  <div id="layoutSidenav">
    <div id="layoutSidenav_content">
      <main>
        <div class="container-fluid px-4">
          <h1 class="mt-4">결재 목록</h1>
          <ol class="breadcrumb mb-4">
            <a class="make-approve" href="/approve/create">결재만들기 </a>
          </ol>
          <div class="card mb-4">
            <div class="card-header">
              <i class="fas fa-table me-1"></i>
              내 결재들
            </div>
            <div class="card-body">
              <button @click="filterApprovalsByStatus(null)">전체</button>
              <button @click="filterApprovalsByStatus(0)">
                기안중 {{ statusCounts.대기중 }}
              </button>
              <button @click="filterApprovalsByStatus(1)">
                진행중 {{ statusCounts.결재자1승인 }}
              </button>
              <button @click="filterApprovalsByStatus(3)">
                반려 {{ statusCounts.반려 }}
              </button>
              <button @click="filterApprovalsByStatus(2)">결재 완료</button>
              <table id="datatablesSimple">
                <thead>
                  <tr>
                    <th>순번</th>
                    <th>기안일자</th>
                    <th>제목</th>
                    <th>기안자</th>
                    <th>결재자1</th>
                    <th>결재자2</th>
                    <th>진행상태</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="approve in filteredApprovals" :key="approve.id" @click="goToApproveReadPage(approve.id)"
                    class="approvelist">
                    <td>{{ approve.id }}</td>
                    <td>{{ approve.createAt }}</td>
                    <td>{{ approve.title }}</td>
                    <td>{{ approve.employeeName }}</td>
                    <td>{{ approve.confirmer1Name }}</td>
                    <td>{{ approve.confirmer2Name }}</td>
                    <td>{{ getStatusText(approve.status) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="pagination">
              <button @click="prevGroup">이전</button>
              <button v-for="page in pageGroup" :key="page" :class="{ active: page === currentPage }"
                @click="changePage(page)">
                {{ page }}
              </button>
              <button @click="nextGroup">다음</button>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import SideBar from "@/components/SideBar.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";

export default {
  name: "ApproveListPage",
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      approvals: [],
      approveLine: [],
      filteredApprovals: [], // 필터링된 결재 목록
      currentFilterStatus: null, // 현재 선택된 필터 상태
      confirmer1: "",
      confirmer2: "",
      currentPage: 1,
      pageSize: "",
      pagesToShow: 5,
      pageGroupStart: 1,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.approvals.length / this.pageSize);
    },
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


    // 상태별 개수를 계산하는 계산된 속성
    statusCounts() {
      const counts = { total: 0, 대기중: 0, 결재자1승인: 0, 최종승인: 0, 반려: 0 };

      // 모든 approvals를 순회하며 상태별로 개수를 계산합니다.
      this.approvals.forEach(approve => {
        counts.total += 1;
        const statusText = this.getStatusText(approve.status);
        if (counts[statusText] !== undefined) {
          counts[statusText] += 1;
        }
      });

      return counts;
    }
  },
  async mounted() {
    await this.fetchApprovals();
    //   // await this.fetchApproveLine();
  },
  methods: {
    changePage(page) {
      // 페이지를 변경하고, 새로운 페이지의 데이터를 불러옵니다.
      this.currentPage = page;
      this.fetchApprovals();
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
    filterApprovalsByStatus(status) {
      this.currentFilterStatus = status;
      if (status === null) {
        this.filteredApprovals = this.approvals;
      } else {
        this.filteredApprovals = this.approvals.filter(approve => approve.status === status);
      }
    },

    async fetchApprovals() {
      const api = `http://localhost:8080/approve/list?page=${this.currentPage - 1}&size=${this.pageSize}`;
      try {
        const response = await axios.get(api);
        if (response.data && response.data.result && Array.isArray(response.data.result.content)) {
          this.approvals = response.data.result.content;
          this.filteredApprovals = this.approvals;
          // this.approvals = Array.isArray(response.data.result) ? response.data.result : [];
          // this.filteredApprovals = this.approvals; // 초기 로딩 시 전체 결재 목록을 보여줍니다.

          // 모든 결재 항목에 대해 결재라인 정보를 불러옵니다.
          // for (const approve of this.approvals) {
          //   await this.fetchApproveLine(approve.id); // 각 결재 항목의 id를 사용하여 fetchApproveLine 호출
          // }
        } else {
          this.approvals = [];
          this.filteredApprovals = [];
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        this.approvals = [];
        this.filteredApprovals = [];
      }
      // const start = (this.currentPage - 1) * this.pageSize;
      //   const end = this.currentPage * this.pageSize;
      //   this.filteredApprovals = this.approvals.slice(start, end);
    },

    goToApproveReadPage(id) {
      if (id) {
        this.$router.push(`/approve/read/${id}`);
      } else {
        console.error("ID is undefined");
      }
    },
    getStatusText(status) {
      const statusMap = {
        0: "대기중",
        1: "결재자1승인",
        2: "최종승인",
        3: "반려",
        4: "회수",
      };
      return statusMap[status] || "알 수 없음";
    },

  },
};
</script>

<style scoped>
.container-fluid {
  padding: 2rem;
}

h1.mt-4 {
  padding-top: 10px;
  margin-left: 1px;
}

/* 브레드크럼 스타일 */
.breadcrumb.mb-4 {
  background-color: #f8f9fa;
  padding: 0.75rem 1rem 50px;
  border-radius: 0.375rem;
}

/* 카드 스타일 */
.card.mb-4 {
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
  display: 100%;
  margin-left: 50px;
}

.card-header {
  background-color: #fff;
  border-bottom: 1px solid #e3e6f0;
  padding: 0.75rem 1.25rem;
}

.card-body {
  padding: 1.25rem;
}

/* 버튼 스타일 */
button {
  font-size: 18px;
  font-weight: 600;
  padding: 5px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111;
  margin: -5px 0px 15px 10px;
}

button:hover {
  background-color: #f75c29;
}

/* 테이블 스타일 */
#datatablesSimple {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
}

#datatablesSimple th,
#datatablesSimple td {
  padding: 0.75rem;
  vertical-align: top;
  border-top: 1px solid #e3e6f0;
}

#datatablesSimple th {
  color: #495057;
  background-color: #f8f9fa;
  border-bottom: 2px solid #e3e6f0;
}

.make-approve {
  position: absolute;
  right: 5%;
  text-decoration: none;
  font-size: 18px;
  font-weight: 600;
  padding: 7px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111;
  margin: -5px 0px 15px 10px;
}

.make-approve:hover {
  background-color: #f75c29;
}

.approvelist {
  cursor: pointer;
  margin: 10px 0;
  transition: color 0.3s ease;
}

.approvelist:hover {
  color: #007bff;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.card mb-4 {}
</style>
