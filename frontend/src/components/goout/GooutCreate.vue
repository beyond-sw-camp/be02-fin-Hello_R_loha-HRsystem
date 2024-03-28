<template>
  <div class="container">
    <div class="header">
      <h1>휴가 신청</h1>
    </div>
    <div class="content">
      <form @submit.prevent="handleFormSubmission">
        <div class="row">
          <div class="label">신청자</div>
          <div class="input">
            <select v-model="employeeId">
              <option
                v-for="employee in employees"
                :key="employee.id"
                :value="employee.id"
              >
                {{ employee.name }}
              </option></select
            ><br />
          </div>
        </div>
        <div class="row">
          <div class="label">대리인</div>
          <div class="input">
            <select v-model="agentId">
              <option
                v-for="agent in employees"
                :key="agent.id"
                :value="agent.id"
              >
                {{ agent.name }}
              </option></select
            ><br />
          </div>
        </div>
        <div class="row">
          <div class="label">근태 사유</div>
          <div class="input-and-remaining-days">
            <select v-model="gooutTypeId" class="select-reason">
              <option
                v-for="gooutType in gooutTypes"
                :key="gooutType.id"
                :value="gooutType.id"
              >
                {{ gooutType.name }}
              </option>
            </select>
            <div v-if="remainingVacationDays !== null" class="remaining-days">
              남은 휴가 일수: {{ remainingVacationDays }}
            </div>
          </div>
        </div>

        <div class="row">
          <div class="label">시작 날짜</div>
          <div class="input"><input type="date" v-model="first" /><br /></div>
        </div>
        <div class="row">
          <div class="label">종료 날짜</div>
          <div class="input"><input type="date" v-model="last" /><br /></div>
        </div>
        <div class="row">
          <div class="label">첨부파일</div>
          <div class="input">
            <input type="file" multiple @change="handleFilesUpload" />
          </div>
        </div>

        <div class="row">
          <div class="label">결재라인</div>
          <div class="input">
            <p>결재자1</p>
            <select v-model="confirmer1Id">
              <option
                v-for="employee in employees"
                :key="employee.id"
                :value="employee.id"
              >
                {{ employee.name }}
              </option></select
            ><br />
            <p>결재자2</p>
            <select v-model="confirmer2Id">
              <option
                v-for="employee in employees"
                :key="employee.id"
                :value="employee.id"
              >
                {{ employee.name }}
              </option></select
            ><br />
          </div>
        </div>
        <div class="row">
          <div class="button">
            <button type="submit">제출</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import jwtDecode from "jwt-decode";

export default {
  data() {
    return {
      backend: "http://192.168.0.51/api",
      gooutTypeId: "",
      agentId: "",
      employeeId: "",
      first: "",
      last: "",
      gooutTypes: [],
      employees: [],
      files: [], // 여러 파일을 저장할 배열
      confirmer1Id: "",
      confirmer2Id: "",
      loggedInUserId: null, // 로그인한 사용자 ID 저장
      sortColumn: null, // 분류할 열 (예: 'name' 또는 'startDate')
      sortOrder: "asc", // 분류 순서 (오름차순 또는 내림차순)
      remainingVacationDays: null, // 남은 휴가 일수
    };
  },

  async created() {
    await this.fetchGooutTypes();
    await this.fetchEmployees();
  },
  mounted() {
    this.setLoggedInUser();
  },

  watch: {
    // 근태 사유(gooutTypeId) 또는 신청자(employeeId)가 변경될 때마다 남은 휴가 일수 조회
    gooutTypeId: {
      handler() {
        this.fetchRemainingVacationDays();
      },
      immediate: true, // 컴포넌트가 마운트될 때 즉시 실행
    },
    employeeId: {
      handler() {
        this.fetchRemainingVacationDays();
      },
      immediate: true, // 컴포넌트가 마운트될 때 즉시 실행
    },
  },

  methods: {
    sortBy(column) {
      if (this.sortColumn === column) {
        this.sortOrder = this.sortOrder === "asc" ? "desc" : "asc";
      } else {
        this.sortColumn = column;
        this.sortOrder = "asc";
      }
    },

    setLoggedInUser() {
      const token = sessionStorage.getItem("token"); // 로컬 스토리지 또는 적절한 저장소에서 토큰 가져오기
      if (token) {
        const decoded = jwtDecode(token);
        this.loggedInUserId = decoded.ID; // 실제 토큰 구조에 따라 변경될 수 있음
      }
    },

    handleFilesUpload(event) {
      this.files = event.target.files; // 선택된 파일들을 files 배열에 저장
    },
    async fetchGooutTypes() {
      const response = await axios.get(`${this.backend}/gooutType/list`);
      this.gooutTypes = response.data.result;
    },
    async fetchEmployees() {
      const response = await axios.get(`${this.backend}/employee/list`);
      this.employees = response.data;
    },
    async createGooutRequest() {
      if (this.confirmer1Id === this.confirmer2Id) {
        alert(
          "결재라인 생성 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다."
        );
        return; // 메소드 실행을 중단
      }
      this.setLoggedInUser();
      let formData = new FormData();
      formData.append(
        "gooutCreateReq",
        new Blob(
          [
            JSON.stringify({
              agentId: this.agentId,
              employeeId: this.employeeId,
              writerId: this.loggedInUserId,
              gooutTypeId: this.gooutTypeId,
              first: this.first,
              last: this.last,
              confirmer1Id: this.confirmer1Id,
              confirmer2Id: this.confirmer2Id,
            }),
          ],
          { type: "application/json" }
        )
      );

      // 여러 파일을 formData에 추가
      for (let i = 0; i < this.files.length; i++) {
        formData.append("uploadFiles", this.files[i]); // 'uploadFiles'로 변경
      }

      try {
        const response = await axios.post(
          `${this.backend}/goout/create`,
          formData
        );
        console.log(response);

        const gooutId = response.data.result;
        console.log("Created goout ID:", gooutId);

        alert("휴가가 등록되었습니다.");
        this.$router.push(`/goout/list`);
      } catch (error) {
        if (error.response && error.response.data) {
          // 서버로부터 받은 에러 메시지를 alert로 표시
          alert(`에러: ${error.response.data.message}`);
        } else {
          alert("휴가 등록 중 문제가 발생했습니다.");
        }
      }
    },

    async handleFormSubmission() {
      await this.createGooutRequest();
    },
    async fetchRemainingVacationDays() {
      if (this.gooutTypeId && this.employeeId) {
        try {
          const response = await axios.get(
            `${this.backend}/goout/remainingVacationDays`,
            {
              params: {
                employeeId: this.employeeId,
                gooutTypeId: this.gooutTypeId,
              },
            }
          );
          this.remainingVacationDays = response.data.result;
          if (this.remainingVacationDays < 0) {
            alert("최대 사용한도를 초과하였습니다. 관리자에게 문의하세요.");
          }
        } catch (error) {
          console.error("남은 휴가 일수를 가져오지 못했습니다.:", error);
        }
      }
    },
  },
};
</script>

<style scoped>
.container {
  width: 800px;
  margin: 20px auto;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.input-and-remaining-days {
  display: flex;
  align-items: center;
  flex: 1;
}

.select-reason {
  width: 50%; /* 근태 사유 선택 창의 크기 조절 */
  margin-right: 10px; /* 남은 휴가 일수와의 간격 조절 */
}

.remaining-days {
  margin-left: auto; /* 남은 휴가 일수를 오른쪽 정렬 */
}

.header {
  background-color: #f7f8fa;
  padding: 10px;
  text-align: center;
}

.header h1 {
  font-size: 20px;
  margin: 0;
}

.content {
  padding: 20px;
}

.row {
  display: flex;
  margin-bottom: 10px;
}

.label {
  width: 100px;
  text-align: right;
  margin-right: 10px;
}

.input {
  flex: 1;
}

input,
select,
textarea {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 5px;
  width: 100%;
}

textarea {
  height: 100px;
}

.button {
  text-align: center;
}

button {
  padding: 5px 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f7f8fa;
  cursor: pointer;
}

button:hover {
  background-color: #ddd;
}
</style>
