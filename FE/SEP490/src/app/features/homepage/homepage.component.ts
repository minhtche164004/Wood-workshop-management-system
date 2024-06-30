import { Component, AfterViewInit, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { WishlistService } from 'src/app/service/wishlist.service';
declare var $: any; // Declare jQuery globally

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements AfterViewInit, OnInit {
  products: any[] = [];
  constructor(private wishList: WishlistService,private productListService: ProductListService, private toastr: ToastrService) { }
  ngOnInit(): void {
    
    this.productListService.getAllProductCustomer().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          console.log('Danh sách sản phẩm:', this.products);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }
  addToWishlist(productId: number) {
    this.wishList.addWishlist(productId)
      .subscribe(
        (response) => {
          console.log('Product added to wishlist:', response);
          this.toastr.success('Sản phẩm đã được thêm vào yêu thích!', 'Thành công'); // Success message
        },
        (error) => {
          console.error('Error adding product to wishlist:', error);
          this.toastr.warning('Vui lòng đăng nhập để thêm sản phẩm yêu thích!', 'Lỗi'); // Error message
        }
      );
  }
  
  

  ngAfterViewInit(): void {
    this.initializeSlickSliders();
    this.initializeBackToTop();
    this.initializeResponsiveNavbar();
    this.initializeCountdownTimer();
  }
  
  private initializeSlickSliders() {
    $('.service-slider').slick({
      autoplay: true,
      autoplaySpeed: 3000,
      pauseOnFocus: false,
      prevArrow: '<span class="left-icon"><i class="fas fa-angle-left"></i><span/>',
      nextArrow: '<span class="right-icon"><i class="fas fa-angle-right"></i><span/>',
      slidesToShow: 1,
      mobileFirst: true,
      responsive: [
        {
          breakpoint: 768,
          settings: {
            slidesToShow: 2
          }
        },
        {
          breakpoint: 1200,
          settings: {
            slidesToShow: 4
          }
        }
      ]
    });

    $('.slider').slick({
      pauseOnFocus: false,
      prevArrow: '<span class="left-icon"><i class="fas fa-angle-left"></i><span/>',
      nextArrow: '<span class="right-icon"><i class="fas fa-angle-right"></i><span/>',
      slidesToShow: 1,
      mobileFirst: true,
      responsive: [
        {
          breakpoint: 768,
          settings: {
            slidesToShow: 2
          }
        },
        {
          breakpoint: 1200,
          settings: {
            slidesToShow: 4
          }
        }
      ]
    });

    $('.nav-item').on('shown.bs.tab', function () {
      $('.slider').slick('refresh');
    });

    $('.list').slick({
      autoplay: true,
      autoplaySpeed: 3000,
      slidesToShow: 1,
      mobileFirst: true,
      prevArrow: '<span class="left-icon"><i class="fas fa-angle-left"></i><span/>',
      nextArrow: '<span class="right-icon"><i class="fas fa-angle-right"></i><span/>',
      responsive: [
        {
          breakpoint: 768,
          settings: {
            arrows: false,
            centerMode: true,
            centerPadding: '40px',
            slidesToShow: 2
          }
        },
        {
          breakpoint: 1200,
          settings: {
            arrows: false,
            centerMode: true,
            centerPadding: '40px',
            slidesToShow: 2
          }
        }
      ]
    });

    $('.news-slider').slick({
      pauseOnFocus: false,
      prevArrow: '<span class="left-icon"><i class="fas fa-angle-left"></i><span/>',
      nextArrow: '<span class="right-icon"><i class="fas fa-angle-right"></i><span/>',
      slidesToShow: 1,
      mobileFirst: true,
      responsive: [
        {
          breakpoint: 768,
          settings: {
            slidesToShow: 2
          }
        },
        {
          breakpoint: 1200,
          settings: {
            slidesToShow: 4
          }
        }
      ]
    });
  }

  private initializeBackToTop() {
    const backtotop = document.querySelector('.backtotop') as HTMLElement;
    window.addEventListener('scroll', function () {
      if (document.documentElement.scrollTop > 300) {
        backtotop.classList.remove('hide');
      } else {
        backtotop.classList.add('hide');
      }
    });
    backtotop.addEventListener('click', function () {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    });
  }

  private initializeResponsiveNavbar() {
    const navbarToggler = document.querySelector('.navbar-toggler') as HTMLElement;
    const responsiveNavbar = document.querySelector('.responsive-navbar') as HTMLElement;
    navbarToggler.addEventListener('click', () => {
      responsiveNavbar.classList.toggle('hide');
    });
  }

  private initializeCountdownTimer() {
    let total = 300000;
    setInterval(() => {
      total--;

      const s = total % 60;
      const m = ((total - s) / 60) % 60;
      const h = Math.floor(total / 3600);
      const d = Math.floor(total / 86400);


    }, 1000);
  }
  
}
