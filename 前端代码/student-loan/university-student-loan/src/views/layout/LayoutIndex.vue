<script setup>
import {
  Management,
  Promotion,
  UserFilled,
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'
import router from '@/router'
import { useUserStore } from '@/stores'
import { onMounted } from 'vue'
const userStore = useUserStore()
onMounted(() => {
  userStore.getUser()
})
// 点击头像的下拉菜单进行跳转
const handleCommand = async (command) => {
  if (command === 'logout') {
    await ElMessageBox.confirm('你确认退出大学生助学贷款系统吗?', '温馨提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    userStore.removeToken()
    userStore.removeUserInfo()
    router.replace('/login')
  } else {
    router.push(`/user/${command}`)
  }
}
</script>
<template>
  <div>
    <el-container class="layout-container-demo" style="height: 100vh">
      <el-aside width="200px">
        <div class="el-aside__logo"></div>
        <el-menu
          text-color="#fff"
          background-color="#d7d4d4"
          active-text-color="var(--el-text-color-primary)"
          :default-active="$route.path"
          router
        >
          <el-menu-item
            index="/loan/apply"
            v-if="userStore.user.usertype === 1"
          >
            <el-icon><Management /></el-icon>
            <span>贷款申请</span>
          </el-menu-item>
          <el-menu-item index="/loan/manage">
            <el-icon><Promotion /></el-icon>
            <span>贷款管理</span>
          </el-menu-item>
          <el-sub-menu index="/user">
            <template #title>
              <el-icon><UserFilled /></el-icon>
              <span>个人中心</span>
            </template>
            <el-menu-item index="/user/info">
              <el-icon><User /></el-icon>
              <span>基本资料</span>
            </el-menu-item>
            <el-menu-item index="/user/avatar">
              <el-icon><Crop /></el-icon>
              <span>更换头像</span>
            </el-menu-item>
            <el-menu-item index="/user/password">
              <el-icon><EditPen /></el-icon>
              <span>重置密码</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header>
          <div>
            江西师范大学：<strong>{{
              userStore.user.nickname || userStore.user.username
            }}</strong>
          </div>
          <el-dropdown placement="bottom-end" @command="handleCommand">
            <span class="el-dropdown__box">
              <el-avatar :src="userStore.user.userPic || avatar" />
              <el-icon><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="info" :icon="User"
                  >基本资料</el-dropdown-item
                >
                <el-dropdown-item command="avatar" :icon="Crop"
                  >更换头像</el-dropdown-item
                >
                <el-dropdown-item command="password" :icon="EditPen"
                  >重置密码</el-dropdown-item
                >
                <el-dropdown-item command="logout" :icon="SwitchButton"
                  >退出登录</el-dropdown-item
                >
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-header>

        <el-main>
          <router-view></router-view>
        </el-main>
        <el-footer>大学生助学贷款系统 ©2023 Created by 朱兆权</el-footer>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped>
/* .layout-container-demo .el-header {
  position: relative;
  background-color: var(--el-color-primary-light-7);
  color: var(--el-text-color-primary);
} */
.layout-container-demo .el-aside {
  color: #d7d4d4;
  background: #d7d4d4;
}
.layout-container-demo .el-menu {
  border-right: none;
}
.layout-container-demo .el-main {
  padding: 0;
}
.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
.el-header {
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  .el-dropdown__box {
    display: flex;
    align-items: center;
    .el-icon {
      color: #999;
      margin-left: 10px;
    }

    &:active,
    &:focus {
      outline: none;
    }
  }
}
.el-aside__logo {
  /* 设置背景图片的路径，相对于当前CSS文件的位置 */
  background-image: url('@/assets/Snipaste_2023-12-11_16-23-09.png');
  background-size: cover;
  width: 100%;
  height: 200px;
}
.el-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #666;
}
</style>
