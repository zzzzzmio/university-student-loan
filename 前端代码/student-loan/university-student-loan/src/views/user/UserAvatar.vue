<script setup>
import { Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { useUserStore } from '@/stores'
import { userAvatar } from '@/api/user'
const uploadRef = ref()
const userStore = useUserStore()
const imgUrl = ref(userStore.user.userPic)

//上传成功
const onUploadFile = (uploadFile) => {
  imgUrl.value = uploadFile.data
}

//上传头像
const onUpdateAvatar = async () => {
  await userAvatar(imgUrl.value)
  ElMessage.success('更换头像成功!')
  userStore.user.userPic = imgUrl.value
}
</script>
<template>
  <page-container title="更换头像">
    <el-card>
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold; font-size: 16px">更换头像</span>
        </div>
      </template>
      <el-row>
        <el-col :span="12">
          <el-upload
            ref="uploadRef"
            class="avatar-uploader"
            auto-upload="true"
            :show-file-list="false"
            action="/api/upload"
            name="file"
            :headers="{ Authorization: userStore.token }"
            :on-success="onUploadFile"
          >
            <img v-if="imgUrl" :src="imgUrl" class="avatar" />
            <img v-else src="@\assets\default.png" width="278" />
          </el-upload>
          <br />
          <el-button
            type="success"
            :icon="Upload"
            size="large"
            @click="onUpdateAvatar"
          >
            上传头像
          </el-button>
        </el-col>
      </el-row>
    </el-card>
  </page-container>
</template>
<style lang="scss" scoped>
.avatar-uploader {
  :deep() {
    .avatar {
      width: 278px;
      height: 278px;
      display: block;
    }
    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }
    .el-upload:hover {
      border-color: var(--el-color-primary);
    }
    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 278px;
      height: 278px;
      text-align: center;
    }
  }
}
</style>
