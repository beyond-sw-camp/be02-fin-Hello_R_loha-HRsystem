<template>

  <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
    <!-- Navbar Brand-->
    <a class="navbar-brand ps-3" href="/main">Hello_R_loha</a>
    <!-- Sidebar Toggle-->
    <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><svg
        class="svg-inline--fa fa-bars" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="bars"
        role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" data-fa-i2svg="">
        <path fill="currentColor"
          d="M0 96C0 78.3 14.3 64 32 64H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32C14.3 128 0 113.7 0 96zM0 256c0-17.7 14.3-32 32-32H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32c-17.7 0-32-14.3-32-32zM448 416c0 17.7-14.3 32-32 32H32c-17.7 0-32-14.3-32-32s14.3-32 32-32H416c17.7 0 32 14.3 32 32z">
        </path>
      </svg><!-- <i class="fas fa-bars"></i> Font Awesome fontawesome.com --></button>
    <!-- Navbar Search-->


    <a class="navbar-brand ps-3" href="/manager" v-if="isAdmin">인사관리</a>

    <!-- Navbar-->
    <ul class="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button" data-bs-toggle="dropdown"
          aria-expanded="false"><svg class="svg-inline--fa fa-user fa-fw" aria-hidden="true" focusable="false"
            data-prefix="fas" data-icon="user" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512"
            data-fa-i2svg="">
            <path fill="currentColor"
              d="M224 256A128 128 0 1 0 224 0a128 128 0 1 0 0 256zm-45.7 48C79.8 304 0 383.8 0 482.3C0 498.7 13.3 512 29.7 512H418.3c16.4 0 29.7-13.3 29.7-29.7C448 383.8 368.2 304 269.7 304H178.3z">
            </path>
          </svg><!-- <i class="fas fa-user fa-fw"></i> Font Awesome fontawesome.com --></a>
        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
          <li><a class="dropdown-item" href="#!">Settings</a></li>
          <li><a class="dropdown-item" href="#!">Activity Log</a></li>
          <li>
            <hr class="dropdown-divider">
          </li>
          <li><a class="dropdown-item" href="#!">Logout</a></li>
        </ul>
      </li>
    </ul>
  </nav>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HeaderComponent',
  components: {

  },
  data() {
    return {
      isAdmin: false,

    };
  },
  methods: {
    checkIsAdmin() {
      console.log("getAuthorizeList START");
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = "http://localhost:8080";
      console.log(api);
      // let formData = new FormData();
      // formData.append('username', this.username);
      // formData.append('password', this.password);
      const token = sessionStorage.getItem("token");
      axios.get(api + "/manager/check", {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      })
        .then((response) => {
          console.log("checkIsAdmin is Loading");
          console.log(response);
          if (response.data.code == 200) {
            this.isAdmin = true;
          }
          else {
            this.isAdmin = false;
          }

        })
        .catch((error) => {
          console.error("Error checkIsAdmin:", error);
          // alert();
          //throw new Error("권한 확인 실패");

        }).finally(()=>{
          console.log("getAuthorizeList END");
        });

    },
  },
  mounted() {
    try {
      this.checkIsAdmin();
    } catch (error) {
      console.error("Error Header Init:", error);
    }
    

  },
}
</script>



<style scoped></style>