<template>
  <div class="homeCamera">
    <div class="show"><p>暂无监控</p></div>
    <ul>
      <li>监控1</li>
      <li>监控2</li>
      <li>监控3</li>
      <li>监控4</li>
    </ul>
  </div>
</template>
<script>
export default {

}
</script>
<style lang="scss" scoped>
.homeCamera {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 3vh;
  .show {
    width: 50%;
    height: 16vh;
    border: 1px solid #41BDFF;
    background: #193847;
    display: flex;
    justify-content: center;
    align-items: center;
    p {
      font-size: 0.2rem;
      text-align: center;
      color: #1f6387;
    }
  }
  ul {
    width: 20%;
    margin-left: 5%;
    li {
      width: 100%;
      // padding: 0.1rem;
      height: 3.5vh;
      line-height: 3.5vh;
      text-align: center;
      color: #44BEFF;
      font-size: 0.16rem;
      background: #193847;
      border: 1px solid #41BDFF;
      margin-top: 0.05rem;
      cursor: pointer;
    }
  }
}
</style>
