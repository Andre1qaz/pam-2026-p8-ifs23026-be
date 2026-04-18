# pam-2026-p8-ifs23026-be

**Delcom Todos Backend API — PAM Praktikum 8**
**Nama:** Andre Christian Saragih
**NIM:** ifs23026 / IFS23026

---

## Perubahan dari Modul 5

- Paginasi pada `GET /todos`: mendukung `?page=` dan `?perPage=`
- Response menyertakan `meta` berisi `page`, `perPage`, `total`, `totalPages`
- Penamaan disesuaikan: author → Andre Christian Saragih, ifs18005 → ifs23026

## Setup

1. Copy `.env.example` ke `.env` dan isi sesuai environment.
2. Jalankan: `./gradlew run`

## API

Sama dengan modul 5 + paginasi di GET /todos.
