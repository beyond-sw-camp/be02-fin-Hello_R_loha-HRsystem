<template>
  <HeaderComponent />
  <div class="sb-nav-fixed">
    <div id="layoutSidenav">
      <SideBar />
      <div id="layoutSidenav_content">
        <main>
          <div id="container-fluid px-4">
            <!-- <h1 class="mt-4">MAIN PAGE</h1> -->
            <div class="row" style="width: 100%">

              <div class="col-xl-10">
                <div class="card mb-6" style="margin-left: 100px;">
                  <div class="card-header">
                    휴가페이지
                  </div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">



                        <div class="all">

                          <router-view></router-view>
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














  
  <!-- <SideBar />

  <div class="all">
      <div class="gooutTypeBanner">
        <div class="gooutTypeBannerTxt">
          <h1>휴가타입</h1>
          <p class="txt2">휴가타입 페이지임돠
            <br>관리자만 들어올 수 있어요</p>
        </div>
  
      </div>
      <router-view></router-view>
    </div> -->
</template>

<script>
import SideBar from '@/components/SideBar.vue'
import HeaderComponent from '@/components/HeaderComponent.vue'
export default {
  name: 'GooutPage',
  components: {
    SideBar,
    HeaderComponent,
  },
}
</script>

<style scoped>
  .gooutTypeBanner {
    margin: 0 auto; /* 수평 중앙 정렬 */
    text-align: center; /* 텍스트 중앙 정렬 */
    width: 100%;
    height: 250px;
    overflow: hidden;
    position: relative;
    background-size: cover;
  }
  
  .gooutTypeBannerTxt {
    margin: 0 auto; /* 수평 중앙 정렬 */
    text-align: center; /* 텍스트 중앙 정렬 */
    display: flex;
    position: absolute;
    left: 50%;
    top: 60%;
    transform: translate(-50%, -50%);
    flex-direction: column;
  }
  .gooutTypeBannerTxt>h1 {
    font-size: 50px;
    font-weight: 600;
    color: #333333;
  }
  
  .gooutTypeBannerTxt>p {
    font-size: 17px;
    color: #333333;
    margin-top: 24px;
    width: 349px;
  }
  
  .txt2 {
    text-align: center; /* 텍스트 중앙 정렬 */
  }
  
  .all{
    padding-left: 225px;
  }
</style>