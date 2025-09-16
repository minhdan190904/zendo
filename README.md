# Create a README file for the user's Zendo project with embedded screenshots

readme = """
# Zendo — Rental Management App (Android, Jetpack Compose)

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue)](#)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-brightgreen)](#)
[![Firebase](https://img.shields.io/badge/Firebase-Firestore%20%7C%20Auth%20%7C%20FCM-orange)](#)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%7C%20MVVM%20%7C%20DI(Hilt)-purple)](#)

**Zendo** là ứng dụng quản lý cho thuê (nhà/phòng/khách thuê/hóa đơn/dịch vụ) xây dựng bằng **Kotlin + Jetpack Compose** theo **Clean Architecture (MVVM + Hilt)**, dữ liệu trên **Firebase (Firestore + Storage)**, xác thực **Firebase Auth (Email/Google)**, thông báo **FCM**, và có tích hợp **Firebase AI / Vertex AI** để truy vấn & gợi ý thông minh trên dữ liệu nhà.

---

## ✨ Chức năng chính
- Quản lý **nhà, phòng, khách thuê, hóa đơn, dịch vụ**.
- **Báo cáo & thống kê** theo biểu đồ Bar/Pie.
- **Upload & lưu trữ ảnh** (phòng, hóa đơn) lên Cloud Storage.
- **Nhắc hạn thanh toán** qua Notification (FCM) & màn hình trong app.
- **Tích hợp Firebase AI** để hỏi/tra cứu dữ liệu nhà một cách tự nhiên.
- Tìm kiếm, lọc, sắp xếp; form động với **validator** (CCCD, số điện thoại, tiền…).
- Làm việc bất đồng bộ bằng **Kotlin Coroutines & Flow**.

---

## 🧱 Tech Stack
- **Ngôn ngữ & UI**: Kotlin, Jetpack Compose, Material Design 3
- **Kiến trúc**: Clean Architecture (domain/data/presentation), MVVM, DI với **Hilt**
- **Dữ liệu**: Firebase Firestore (NoSQL), Cloud Storage
- **Xác thực**: Firebase Auth (Email/Google)
- **Notification & Logic**: Firebase Cloud Functions, FCM
- **Khác**: Retrofit/OkHttp, Coil/Glide (tải ảnh), Timber/Logger, WorkManager (tác vụ nền)

---

## 📸 Screenshots

<div align="center">
  <img src="https://github.com/user-attachments/assets/345251ab-7b1c-4548-b219-64747c6f57f5" alt="Overview" width="280">
  <img src="https://github.com/user-attachments/assets/8d9e2c0f-053a-4532-b330-403d188c857f" alt="List & Detail" width="280">
  <img src="https://github.com/user-attachments/assets/fb64f6cd-fb09-41c1-9857-acdbf0a0cc40" alt="Charts" width="280">
  <br/>
  <img src="https://github.com/user-attachments/assets/88730595-e6f3-4a35-a380-c69ec4f61180" alt="Forms" width="280">
  <img src="https://github.com/user-attachments/assets/533fb319-6afa-4761-9b43-5b3b1810d0f5" alt="Room/Tenant" width="280">
  <img src="https://github.com/user-attachments/assets/747c33b7-0ecb-4858-b4a3-7051a5bfe187" alt="Invoices" width="280">
  <br/>
  <img src="https://github.com/user-attachments/assets/26e6c29c-b3f1-4104-84b4-88cfd4c090df" alt="AI Assistant" width="280">
  <img src="https://github.com/user-attachments/assets/d39f003d-8b5b-40a9-9c42-71abe723810b" alt="Notifications" width="280">
</div>

> *Ảnh chỉ minh họa: một số màn hình có thể thay đổi nhẹ theo từng phiên bản.*

---

## 🏗️ Cấu trúc dự án (rút gọn)

