<script setup>
import { ref } from 'vue'
import { userPwd } from '@/api/user'
import { useUserStore } from '@/stores'
import router from '@/router'
const userStore = useUserStore()
const form = ref()
const formModel = ref({
  old_pwd: '',
  new_pwd: '',
  re_pwd: ''
})
const checkOldSame = (rule, value, cb) => {
  if (value === formModel.value.old_pwd) {
    cb(new Error('原密码和新密码不能一样!'))
  } else {
    cb()
  }
}

const checkNewSame = (rule, value, cb) => {
  if (value !== formModel.value.new_pwd) {
    cb(new Error('新密码和确认再次输入的新密码不一样!'))
  } else {
    cb()
  }
}
const rules = {
  // 原密码
  old_pwd: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码长度必须是6-15位的非空字符串',
      trigger: 'blur'
    }
  ],
  // 新密码
  new_pwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码长度必须是6-15位的非空字符串',
      trigger: 'blur'
    },
    { validator: checkOldSame, trigger: 'blur' }
  ],
  // 确认新密码
  re_pwd: [
    { required: true, message: '请再次确认新密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码长度必须是6-15位的非空字符串',
      trigger: 'blur'
    },
    { validator: checkNewSame, trigger: 'blur' }
  ]
}

//重置
const onReset = () => {
  formModel.value = {
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
  }
}

//提交
const onSubmit = async () => {
  await form.value.validate()
  await userPwd(formModel.value)
  ElMessage.success('重置密码成功!')
  userStore.removeToken()
  userStore.removeUserInfo()
  router.push('/login')
}
</script>
<template>
  <page-container title="重置密码">
    <el-card>
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold; font-size: 16px">重置密码</span>
        </div>
      </template>
      <el-row>
        <el-col :span="12">
          <el-form
            label-width="100px"
            size="large"
            :model="formModel"
            :rules="rules"
            ref="form"
          >
            <el-form-item label="原密码" prop="old_pwd">
              <el-input v-model="formModel.old_pwd" type="password"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="new_pwd">
              <el-input v-model="formModel.new_pwd" type="password"></el-input>
            </el-form-item>
            <el-form-item label="确认新密码" prop="re_pwd">
              <el-input v-model="formModel.re_pwd" type="password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="onSubmit" type="primary">修改密码</el-button>
              <el-button @click="onReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </page-container>
</template>
