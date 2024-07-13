import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ProductListService } from 'src/app/service/product/product-list.service';

@Component({
  selector: 'app-order-rq-detail',
  templateUrl: './order-rq-detail.component.html',
  styleUrls: ['./order-rq-detail.component.scss']
})
export class OrderRqDetailComponent {
  uploadForm: FormGroup;
  selectedThumbnail: File | null = null;
  selectedImages: File[] = [];

  constructor(private fb: FormBuilder, private productListService: ProductListService) {
    this.uploadForm = this.fb.group({
      product_name: [''],
      description: [''],
      price: [0],
      
      category_id: [0],
      type: [0]
    });
  }

  onThumbnailSelected(event: any): void {
    this.selectedThumbnail = event.target.files[0];
  }

  onImagesSelected(event: any): void {
    this.selectedImages = Array.from(event.target.files);
  }

  onSubmit(): void {
    if (this.uploadForm.valid && this.selectedThumbnail && this.selectedImages.length) {
      const productData = this.uploadForm.value;
      console.log('Form Data:', productData);
      console.log('Selected Thumbnail:', this.selectedThumbnail);
      console.log('Selected Images:', this.selectedImages);

      this.productListService.uploadProduct(productData, this.selectedThumbnail, this.selectedImages)
        .subscribe(response => {
          console.log('Upload successful', response);
        }, error => {
          console.error('Upload error', error);
        });
    }
  }
}
