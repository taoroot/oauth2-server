<template>
  <div class="app-container">
    <el-row>
      <!-- 左侧 -->
      <el-col :span="8" style="padding-top: 20px; padding-left: 20px">
        <!-- 左侧-第1行 -->
        <el-row>
          <my-panel />
          <!-- <el-col :span="12">
            <svg-icon icon-class="sun" style="width:40px;height:40px;" /> 40%
          </el-col>
          <el-col :span="12">
            <svg-icon icon-class="light" style="width:40px;height:40px;" /> 40%
          </el-col> -->
        </el-row>
        <!-- 左侧-第2行 -->
        <el-row style="background:#fff;margin-right: 20px; padding:16px 16px;margin-bottom:32px; height: 200px;">
          <my-echart id="echart-1" height="200px" :chart-data="lineChartData" />
        </el-row>
        <!-- 左侧-第3行 -->
        <el-row>
          <my-data />
        </el-row>
      </el-col>
      <!-- 中部 -->
      <el-col :span="8" style="padding-top: 40px;">
        <my-map />
      </el-col>
      <!-- 右侧 -->
      <el-col :span="8">
        <el-row>
          <my-camera />
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import MyEchart from './components/MyEchart'
import MyMap from './components/MyMap'
import MyData from './components/MyData'
import MyCamera from './components/MyCamera'
import MyPanel from './components/MyPanel'
export default {
  components: {
    MyMap, MyEchart, MyData,
    MyCamera, MyPanel
  },
  data: function() {
    return {
      lineChartData: {
        expectedData: [130, 140, 141, 142, 145, 150, 160],
        actualData: [120, 82, 91, 154, 162, 140, 130]
      }
    }
  },
  created() {
    for (let i = 0; i < 3; i++) {
      setTimeout(() => {
        if (document.querySelectorAll('.Point')) {
          document.querySelectorAll('.Point').forEach((item) => {
            document.body.removeChild(item)
          })
        }
        if (document.querySelector('#scene')) {
          document.body.removeChild(document.querySelector('#scene'))
        }
        console.log('delete')
      }, i * 1000)
    }
  }
}
</script>
<style lang="scss" scoped>
.app-container{
  padding: 0;
  padding-top: 34px;
}
</style>

