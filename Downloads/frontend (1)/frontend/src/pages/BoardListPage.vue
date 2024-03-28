<template>
  <HeaderComponent />

  <div class="sb-nav-fixed">
    <div id="layoutSidenav">
      <SideBar />
      <div id="layoutSidenav_content">
        <main>
          <div id="container-fluid px-4">
            <div class="row" style="width: 100%; margin: auto">
              <div class="col-xl-12">
                <div class="card mb-3">
                  <div class="card-header">공지사항</div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div class="container">
                          <table class="table">
                            <thead>
                              <tr>
                                <th>ID</th>
                                <th>제목</th>
                                <th>내용</th>
                                <th>작성자</th>
                                <th>날짜</th>
                                <th>파일</th>
                              </tr>
                            </thead>
                            <tbody>
                              <tr
                                v-for="board in boards"
                                :key="board.id"
                                @click="viewBoardRead(board.id)"
                                style="cursor: pointer"
                              >
                                <td>{{ board.id }}</td>
                                <td>{{ board.title }}</td>
                                <td>{{ board.text }}</td>
                                <td>{{ board.name }}</td>
                                <td>{{ board.date }}</td>
                                <td>
  <a v-if="board.filename" :href="`/board/read/${board.id}`">
    {{ board.filename.split('_')[1] }}
  </a>
  <span v-else>No File</span>
</td>
                              </tr>
                            </tbody>
                          </table>

                          <div class="pagination">
                            <a href="#" class="prev">&laquo; 이전</a>
                            <!-- 페이지 버튼은 자동으로 생성됩니다. -->
                            <a href="#" class="next">다음 &raquo;</a>
                          </div>
                        </div>
                      </div>
                      <div class="chartjs-size-monitor-shrink">
                        <div class=""></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script>
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";
import axios from "axios";

export default {
  name: "BoardListPage",
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      boards: [],
    };
  },
  methods: {
    viewBoardRead(id) {
      window.location.href = `/board/read/${id}`;
    },
    fetchBoardData(page) {
      const itemsPerPage = 6;
      axios
        .get(`http://localhost:8080/board/check?page=${page}&perPage=${itemsPerPage}`)
        .then((response) => {
          this.boards = response.data.result;
        })
        .catch((error) => {
          console.error("Error fetching board data:", error);
        });
    },
  },
  mounted() {
    this.fetchBoardData(1);
  },
};
</script>

<style scoped>
#commuteButton {
  border: none;
  outline: none;
  margin: 10px;
  font-size: 12px;
  background-color: #f7c231;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 80%;
  height: 35px;
}

#leaveButton {
  border: none;
  outline: none;
  margin: 10px;
  font-size: 12px;
  background-color: #f7c231;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 80%;
  height: 35px;
}

button:hover {
  background-color: #f7a129;
}

button:active {
  background-color: #ff8c00;
}

#commute-info {
  height: 100%;
  font-size: 12px;
  text-align: center;
  border: 2px solid #f0f0f0;
  max-width: 220px;
  margin: auto;
  border-radius: 10px;
  background-color: #f9f9f9;
  position: relative;
}

.time {
  font-size: 15px;
}

.profile-pic {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 2px solid #fff;
  position: absolute;
  top: 75px;
  left: 50%;
  transform: translateX(-50%);
}

.main-button-container {
  position: relative;
  padding: 0px;
  margin-top: 150px;
}

#sumTime {
  position: relative;
  color: gray;
}
body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
  margin: 0;
  padding: 0;
}

.container {
  /* max-width: 970px; */
  margin: auto;
  padding: 8px;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  overflow-x: auto;
  font-size: 10px;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.table th {
  background-color: #f2f2f2;
}

.table tr:last-child td {
  border-bottom: none;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.pagination a {
  display: inline-block;
  padding: 8px 16px;
  text-decoration: none;
  color: #333;
  border: 1px solid #ddd;
  margin-right: 5px;
}

.pagination a.active {
  background-color: #e29c41;
  color: white;
  border: 1px solid #e29c41;
}
</style>