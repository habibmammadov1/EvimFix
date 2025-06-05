// State Management
const State = {
    favorites: JSON.parse(localStorage.getItem('favorites')) || [],
    visibleProperties: 8,
    isLoading: false,
    allCards: document.querySelectorAll('.product-card'),
    
    addFavorite(property) {
        const existingIndex = this.favorites.findIndex(fav => fav.id === property.id);
        
        if (existingIndex === -1) {
            this.favorites.push(property);
            showNotification('Sevimlilərə əlavə edildi');
        } else {
            this.favorites.splice(existingIndex, 1);
            showNotification('Sevimlilərdən silindi');
        }
        this.saveFavorites();
    },
    
    saveFavorites() {
        localStorage.setItem('favorites', JSON.stringify(this.favorites));
    }
};

// Mobile Menu Toggle
function showMenu() {
    const navList = document.querySelector('.nav-list');
    const mobileMenu = document.querySelector('.mobile-menu');
    navList.classList.toggle('active');
    mobileMenu.classList.toggle('active');
}

// Notification System
function showNotification(message) {
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.classList.add('fade-out');
        setTimeout(() => notification.remove(), 300);
    }, 2000);
}

// Initialize Favorites
function updateFavoriteButton(button, propertyId) {
    const isFavorited = State.favorites.some(fav => fav.id === propertyId);
    button.classList.toggle('favorited', isFavorited);
    const heartImage = button.querySelector('img');
    console.log(isFavorited);
    if (heartImage) {
        heartImage.src = isFavorited ? '/website/img/heartFilled.png' : '/website/img/heart.png';
    }

    console.log(isFavorited + ' - ' + heartImage.src);
}

// Infinite Scroll Implementation
function initInfiniteScroll() {
    const sentinel = document.createElement('div');
    sentinel.id = 'scroll-sentinel';
    sentinel.style.height = '100px';
    sentinel.style.width = '100%';
    document.querySelector('#product .container').appendChild(sentinel);

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting && !State.isLoading && State.visibleProperties < State.allCards.length) {
                loadMoreProperties();
            }
        });
    }, {
        rootMargin: '100px',
        threshold: 0.1
    });

    observer.observe(sentinel);
}

// Load More Properties Functionality
async function loadMoreProperties() {
    if (State.isLoading || State.visibleProperties >= State.allCards.length) return;
    
    State.isLoading = true;
    
    try {
        // Show loading state (optional - could add a loading spinner)
        showCards();
        
        // Simulate network delay (remove in production)
        await new Promise(resolve => setTimeout(resolve, 500));
        
        State.visibleProperties += 8;
        showCards();
    } catch (error) {
        console.error('Error loading properties:', error);
        showNotification('Xəta baş verdi. Yenidən cəhd edin.');
    } finally {
        State.isLoading = false;
    }
}

// Show/Hide Cards Based on Visible Count
function showCards() {
    State.allCards.forEach((card, index) => {
        card.style.display = (index < State.visibleProperties) ? "block" : "none";
    });
}

// Property Card Setup
function setupPropertyCards() {
    State.allCards.forEach(card => {
        // Ensure each card has a unique ID
        if (!card.dataset.propertyId) {
            card.dataset.propertyId = 'prop-' + Math.random().toString(36).substr(2, 9);
        }

        const favoriteBtn = card.querySelector('.favorite-btn');
        if (favoriteBtn) {
            // Initialize button state
            updateFavoriteButton(favoriteBtn, card.dataset.propertyId);
            
            // Click handler for favorite button
            favoriteBtn.addEventListener('click', (e) => {
                e.stopPropagation();
                const property = {
                    id: card.dataset.propertyId,
                    title: card.querySelector('.location-name')?.textContent || '',
                    price: card.querySelector('.price p')?.textContent || '',
                    image: card.querySelector('.product-img')?.src || ''
                };
                State.addFavorite(property);
                updateFavoriteButton(favoriteBtn, card.dataset.propertyId);
            });
        }

        //Click handler for entire card
        card.addEventListener('click', function(event) {
            if (event.target.closest('.favorite-btn')) return;
            window.location.href = 'property-detail.html?id=' + card.dataset.propertyId;
        });
    });
}

// Initialize everything
document.addEventListener('DOMContentLoaded', () => {
    setupPropertyCards();
    showCards();
    initInfiniteScroll();
    
    // Remove the "Load More" button if it exists
    const loadMoreBtn = document.getElementById('loadMoreBtn');
    if (loadMoreBtn) {
        loadMoreBtn.remove();
    }
});

// Utility Functions
function debounce(func, timeout = 300) {
    let timer;
    return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => { func.apply(this, args); }, timeout);
    };
}