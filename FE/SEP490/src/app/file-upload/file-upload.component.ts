import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent {
  selectedFile: File | null = null;

  constructor(private http: HttpClient) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onUpload() {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      // Replace with your actual backend API endpoint
      this.http.post('http://your-backend-api.com/upload', formData)
        .subscribe(
          (response) => {
            console.log('Upload successful!', response);
            this.selectedFile = null; // Clear selection after successful upload
          },
          (error) => {
            console.error('Upload failed:', error);
          }
        );
    } else {
      console.error('Please select a file to upload.');
    }
  }
}
