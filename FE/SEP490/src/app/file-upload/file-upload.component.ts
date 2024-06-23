import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent implements OnInit {
  constructor(private http: HttpClient) { }

  ngOnInit(): void { }

  onChangeFile(event: any) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      const formData = new FormData();
      formData.append('file', file);

      this.http.post('http://localhost:8080/api/auth/product/upload', formData).subscribe((res: any) => {
        console.log('File uploaded successfully:', res);
      });
    }
  }
}
