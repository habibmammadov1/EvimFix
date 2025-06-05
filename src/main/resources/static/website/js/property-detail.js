const prevBtn = document.querySelector('.prev-btn');
const nextBtn = document.querySelector('.next-btn');
const slides = document.querySelectorAll('.property-slide');
const slider = document.querySelector('.property-images');
let currentIndex = 0;
let touchStartX = 0;
let touchEndX = 0;

// Initialize indicators (run once on page load)
function initIndicators() {
  const indicatorsContainer = document.createElement('div');
  indicatorsContainer.className = 'slide-indicators';
  
  slides.forEach((_, index) => {
    const indicator = document.createElement('div');
    indicator.className = 'slide-indicator';
    indicator.addEventListener('click', () => {
      currentIndex = index;
      updateSlides();
    });
    indicatorsContainer.appendChild(indicator);
  });
  
  slider.appendChild(indicatorsContainer);
}

// Mobile Menu Toggle
function showMenu() {
    const navList = document.querySelector('.nav-list');
    const mobileMenu = document.querySelector('.mobile-menu');
    navList.classList.toggle('active');
    mobileMenu.classList.toggle('active');
}

// Update slides + indicators
function updateSlides() {
  slides.forEach((slide, index) => {
    slide.style.display = (index === currentIndex) ? 'block' : 'none';
  });
  updateIndicators(); // Sync dots
}

// Update active indicator
function updateIndicators() {
  const indicators = document.querySelectorAll('.slide-indicator');
  indicators.forEach((indicator, index) => {
    indicator.classList.toggle('active', index === currentIndex);
  });
}

// Navigation
prevBtn.addEventListener('click', () => {
  currentIndex = (currentIndex - 1 + slides.length) % slides.length;
  updateSlides();
});

nextBtn.addEventListener('click', () => {
  currentIndex = (currentIndex + 1) % slides.length;
  updateSlides();
});

// Keyboard support
document.addEventListener('keydown', (e) => {
  if (e.key === 'ArrowLeft') prevBtn.click();
  if (e.key === 'ArrowRight') nextBtn.click();
});

// Touch swipe
slider.addEventListener('touchstart', (e) => {
  touchStartX = e.changedTouches[0].screenX;
}, { passive: true });

slider.addEventListener('touchend', (e) => {
  touchEndX = e.changedTouches[0].screenX;
  handleSwipe();
}, { passive: true });

function handleSwipe() {
  if (touchEndX < touchStartX - 30) nextBtn.click(); // Swipe left = next
  if (touchEndX > touchStartX + 30) prevBtn.click(); // Swipe right = prev
}

// Initialize everything
initIndicators(); // Create dots
updateSlides();   // Show first slide

// Preload images (add at the bottom of your JS)
window.addEventListener('load', () => {
    document.querySelectorAll('.property-slide').forEach(img => {
        new Image().src = img.src; 
    });
});

const header = document.querySelector("[data-header]");
const backTopBtn = document.querySelector("[data-back-top-btn]");

const activeElementOnScroll = function () {
  if (window.scrollY > 100) {
    header.classList.add("active");
    backTopBtn.classList.add("active");
  } else {
    header.classList.remove("active");
    backTopBtn.classList.remove("active");
  }
}