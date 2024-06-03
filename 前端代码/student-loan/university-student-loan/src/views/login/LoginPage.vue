<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref, watch } from 'vue'
import { userRegisiter, userLogin } from '@/api/user'
import router from '@/router'
import { useUserStore } from '@/stores'
const userStore = useUserStore()
const isRegister = ref(false)
const form = ref()
const formModel = ref({
  username: '',
  password: '',
  usertype: '',
  repassword: ''
})

//校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    {
      min: 5,
      max: 16,
      message: '用户名应为5-16个大小写字母和数字',
      trigger: 'blur'
    }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{5,16}$/,
      message: '密码应为5-16个非空字符',
      trigger: 'blur'
    }
  ],
  repassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      pattern: /^\S{5,16}$/,
      message: '密码应为5-16个非空字符',
      trigger: 'blur'
    },
    {
      validator: (rules, value, callback) => {
        if (value !== formModel.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  usertype: [{ required: true, message: '请选择用户类型', trigger: 'change' }]
}

//注册
const register = async () => {
  await form.value.validate()
  await userRegisiter(formModel.value)
  ElMessage.success('注册成功！')
  isRegister.value = false
}

//登录
const login = async () => {
  await form.value.validate()
  const {
    data: { data }
  } = await userLogin(formModel.value)
  userStore.setToken(data)
  ElMessage.success('登录成功！')
  router.replace('/loan/manage')
}

// 监测防止登录跳转到注册携带数据
watch(isRegister, () => {
  formModel.value = {
    username: '',
    password: '',
    repassword: ''
  }
})
</script>
<template>
  <el-row class="login-page">
    <el-col :span="12" class="bg"></el-col>
    <el-col :span="6" :offset="3" class="form">
      <!-- 注册 -->
      <el-form
        ref="form"
        size="large"
        autocomplete="off"
        v-if="isRegister"
        :model="formModel"
        :rules="rules"
      >
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            :prefix-icon="User"
            placeholder="请输入用户名"
            v-model="formModel.username"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            :prefix-icon="Lock"
            placeholder="请输入密码"
            type="password"
            v-model="formModel.password"
          ></el-input>
        </el-form-item>
        <el-form-item prop="repassword">
          <el-input
            :prefix-icon="Lock"
            placeholder="请再次输入密码"
            type="password"
            v-model="formModel.repassword"
          ></el-input>
        </el-form-item>
        <el-form-item prop="usertype">
          <el-select placeholder="请选择用户类型" v-model="formModel.usertype">
            <el-option label="学生" value="1" />
            <el-option label="校方" value="2" />
            <el-option label="后台管理人员" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            class="button"
            type="primary"
            auto-insert-space
            @click="register"
          >
            注册
          </el-button>
        </el-form-item>
        <el-form-item class="flex">
          <el-link type="info" :underline="false" @click="isRegister = false">
            ← 返回
          </el-link>
        </el-form-item>
      </el-form>
      <!-- 登录 -->
      <el-form
        size="large"
        autocomplete="off"
        ref="form"
        :rules="rules"
        :model="formModel"
        v-else
      >
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>
        <el-form-item prop="username">
          <el-input
            :prefix-icon="User"
            placeholder="请输入用户名"
            v-model="formModel.username"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            :prefix-icon="Lock"
            placeholder="请输入密码"
            type="password"
            v-model="formModel.password"
          ></el-input>
        </el-form-item>
        <el-form-item prop="usertype">
          <el-select placeholder="请选择用户类型" v-model="formModel.usertype">
            <el-option label="学生" value="1" />
            <el-option label="校方" value="2" />
            <el-option label="后台管理人员" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox>记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            class="button"
            type="primary"
            auto-insert-space
            @click="login"
            >登录</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-form-item class="flex">
            <el-link type="info" :underline="false" @click="isRegister = true">
              注册 →
            </el-link>
          </el-form-item>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>
<style scoped lang="scss">
.login-page {
  height: 100vh;
  background-color: #fff;
  .bg {
    background: url('@/assets/01f8525e8aa1aba801216518be16b7.png@1280w_1l_2o_100sh.png')
      no-repeat center / cover;
  }
  .form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;
    .title {
      margin: 0 auto;
    }
    .button {
      width: 100%;
    }
    .flex {
      width: 100%;
      display: flex;
      justify-content: space-between;
    }
  }
}
</style>
