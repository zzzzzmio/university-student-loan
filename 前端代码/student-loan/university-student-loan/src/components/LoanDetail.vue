<script setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
// import axios from 'axios'
import { loanApply, loanDetail, updateUserLoan } from '@/api/loan'
import { useUserStore } from '@/stores'
const form = ref()
const editorRef = ref()
const formModel = ref({
  loanType: '',
  loanLevel: '',
  loanMaterial: '',
  content: ''
})
const userStore = useUserStore()
const rules = {
  loanType: [
    { required: true, messsage: '请选择资助金申请类型', trigger: 'blur' }
  ],
  loanLevel: [
    { required: true, messsage: '请选择资助金等级', trigger: 'blur' }
  ],
  loanMaterial: [
    { required: true, messsage: '证明材料不能为空', trigger: 'blur' }
  ],
  content: [{ required: true, messsage: '自我简述不能为空', trigger: 'blur' }]
}
const update = ref(false)

//给贷款管理提供一个方法传递贷款的详细内容
const open = async (row) => {
  update.value = true
  const {
    data: { data }
  } = await loanDetail(row.id)
  formModel.value = data
}

//上传照片
const onUploadFile = (uploadFile) => {
  formModel.value.loanMaterial = uploadFile.data
}

//提交
const submit = async () => {
  await form.value.validate
  await loanApply(formModel.value)
  ElMessage.success('申请成功!')
  formModel.value = {
    loanType: '',
    loanLevel: '',
    loanMaterial: '',
    content: ''
  }
  editorRef.value.setHTML('')
}
const emit = defineEmits(['success'])

//更新
const updateLoan = async () => {
  await updateUserLoan(formModel.value)
  ElMessage.success('更新成功!')
  emit('success')
}
defineExpose({
  open
})
</script>
<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span style="font-weight: bold; font-size: 16px">贷款申请</span>
      </div>
    </template>
    <el-form class="detail" :model="formModel" :rules="rules" ref="form">
      <el-form-item label="资助金类型" prop="loanType">
        <el-select placeholder="请选择资助金类型" v-model="formModel.loanType">
          <el-option label="奖学金" :value="1"></el-option>
          <el-option label="助学金" :value="2"></el-option>
          <el-option label="助学贷款" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="资助金等级" prop="loanLevel">
        <el-select placeholder="请选择资助金等级" v-model="formModel.loanLevel">
          <el-option label="一级" :value="1"></el-option>
          <el-option label="二级" :value="2"></el-option>
          <el-option label="三级" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="证明材料" prop="loanMaterial">
        <el-upload
          class="avatar-uploader"
          auto-upload="true"
          :show-file-list="false"
          action="/api/upload"
          name="file"
          :headers="{ Authorization: userStore.token }"
          :on-success="onUploadFile"
          v-if="userStore.user.usertype === 1"
        >
          <img
            v-if="formModel.loanMaterial"
            :src="formModel.loanMaterial"
            class="avatar"
          />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <el-image
          v-else
          :src="formModel.loanMaterial"
          :preview-src-list="[formModel.loanMaterial]"
          previewOnClick
        ></el-image>
      </el-form-item>
      <el-form-item label="自我简述" prop="content">
        <div class="editor">
          <quill-editor
            theme="snow"
            contentType="html"
            v-model:content="formModel.content"
            ref="editorRef"
          >
          </quill-editor>
        </div>
      </el-form-item>
      <el-form-item v-if="userStore.user.usertype === 1">
        <el-button type="primary" @click="submit" v-if="!update"
          >提交申请</el-button
        >
        <el-button type="primary" v-else @click="updateLoan"
          >提交修改</el-button
        >
      </el-form-item>
    </el-form>
  </el-card>
</template>
<style scoped>
.detail {
  height: 100vh;
}
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
.editor {
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
