<script setup>
import { useUserStore } from '@/stores'
import { userUpdate } from '@/api/user'
import { ref } from 'vue'
const form = ref()
const userStore = useUserStore()
const formModel = ref({})
formModel.value = userStore.user
const rules = {
  nickname: [
    { required: true, message: '请输入用户昵称', trigger: 'blur' },
    {
      pattern: /^\S{2,10}$/,
      message: '昵称必须是2-10位的非空字符串',
      trigger: 'blur'
    }
  ],
  email: [
    { required: true, message: '请输入用户邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ]
}

//提交
const onSubmit = async () => {
  await form.value.validate
  await userUpdate(formModel.value)
  ElMessage.success('更新成功!')
  userStore.getUser()
}
</script>
<template>
  <page-container title="基本资料">
    <el-card>
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold; font-size: 16px">基本资料</span>
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
            <el-form-item label="登录名称">
              <el-input v-model="formModel.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="用户昵称" prop="nickname">
              <el-input v-model="formModel.nickname"></el-input>
            </el-form-item>
            <el-form-item label="用户邮箱" prop="email">
              <el-input v-model="formModel.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">提交修改</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </page-container>
</template>
