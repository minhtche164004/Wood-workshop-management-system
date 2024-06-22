import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trườngg

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {

  selectedFile: File | undefined;

  constructor(private http: HttpClient) { }

  onFileChanged(event: any) {
    this.selectedFile = event.target.files[0]; // Lấy tệp tin từ sự kiện change của input file
  }

  onUpload() {
    if (!this.selectedFile) {
      console.error('No file selected');
      return;
    }

    const uploadData = new FormData(); // FormData để gửi dữ liệu lên server
    uploadData.append('file', this.selectedFile, this.selectedFile.name); // Thêm tệp tin vào FormData

    // Gửi yêu cầu POST đến API để tải lên tệp tin
    this.http.post<string>(`${environment.apiUrl}api/auth/product/upload`, uploadData)
      .subscribe(
        imageUrl => {
          console.log('File uploaded successfully:', imageUrl);
          // Xử lý response tại đây, ví dụ: lưu đường dẫn tệp tin vào database hoặc hiển thị trên giao diện người dùng
        },
        error => {
          console.error('Error uploading file:', error);
        }
      );
  }
}
