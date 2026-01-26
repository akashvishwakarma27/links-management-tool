/**
 * Frontend Error Prevention and Monitoring System
 * Prevents JavaScript errors and provides comprehensive error handling
 */

// Global Error Handler
window.addEventListener('error', function(event) {
    console.error('Global error caught:', event.error);
    handleError(event.error, 'JavaScript Error');
    event.preventDefault();
});

// Unhandled Promise Rejection Handler
window.addEventListener('unhandledrejection', function(event) {
    console.error('Unhandled promise rejection:', event.reason);
    handleError(event.reason, 'Promise Rejection');
    event.preventDefault();
});

// Error Handling System
class ErrorPreventionSystem {
    constructor() {
        this.errors = [];
        this.maxErrors = 50;
        this.retryAttempts = new Map();
        this.circuitBreakers = new Map();
        this.performanceMetrics = {
            apiCalls: 0,
            errors: 0,
            averageResponseTime: 0
        };
        
        this.initializeErrorHandling();
        this.startPerformanceMonitoring();
    }
    
    initializeErrorHandling() {
        // Override console.error to catch all errors
        const originalError = console.error;
        console.error = function(...args) {
            originalError.apply(console, args);
            if (args[0] instanceof Error) {
                window.errorPrevention.handleError(args[0], 'Console Error');
            }
        };
        
        // Monitor fetch requests
        this.interceptFetch();
        
        // Monitor DOM mutations
        this.monitorDOM();
    }
    
    interceptFetch() {
        const originalFetch = window.fetch;
        window.fetch = async function(...args) {
            const startTime = performance.now();
            const url = args[0];
            
            try {
                window.errorPrevention.performanceMetrics.apiCalls++;
                
                // Check circuit breaker
                if (window.errorPrevention.isCircuitOpen(url)) {
                    throw new Error('Circuit breaker is open for: ' + url);
                }
                
                const response = await originalFetch.apply(this, args);
                
                const endTime = performance.now();
                const responseTime = endTime - startTime;
                window.errorPrevention.updatePerformanceMetrics(responseTime);
                
                // Reset circuit breaker on success
                window.errorPrevention.resetCircuitBreaker(url);
                
                return response;
                
            } catch (error) {
                const endTime = performance.now();
                const responseTime = endTime - startTime;
                window.errorPrevention.updatePerformanceMetrics(responseTime);
                
                window.errorPrevention.handleNetworkError(error, url);
                throw error;
            }
        };
    }
    
    monitorDOM() {
        // Monitor for DOM errors
        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.type === 'childList') {
                    mutation.addedNodes.forEach((node) => {
                        if (node.nodeType === Node.ELEMENT_NODE) {
                            this.validateElement(node);
                        }
                    });
                }
            });
        });
        
        observer.observe(document.body, {
            childList: true,
            subtree: true
        });
    }
    
    validateElement(element) {
        // Check for common issues
        if (element.tagName === 'IMG' && !element.src) {
            console.warn('Image element without src found:', element);
        }
        
        if (element.tagName === 'A' && !element.href && !element.onclick) {
            console.warn('Link element without href or onclick found:', element);
        }
        
        // Check for missing required attributes
        if (element.hasAttribute('required') && !element.value) {
            console.warn('Required element without value found:', element);
        }
    }
    
    handleError(error, type) {
        const errorInfo = {
            timestamp: new Date().toISOString(),
            type: type,
            message: error.message || error,
            stack: error.stack,
            url: window.location.href,
            userAgent: navigator.userAgent,
            online: navigator.onLine
        };
        
        this.errors.push(errorInfo);
        
        // Keep only recent errors
        if (this.errors.length > this.maxErrors) {
            this.errors.shift();
        }
        
        this.performanceMetrics.errors++;
        
        // Log error
        console.error('Error caught by prevention system:', errorInfo);
        
        // Send to monitoring service (in production)
        this.reportError(errorInfo);
        
        // Show user-friendly message
        this.showUserFriendlyMessage(error);
    }
    
    handleNetworkError(error, url) {
        const errorKey = `network_${url}`;
        const attempts = this.retryAttempts.get(errorKey) || 0;
        
        this.retryAttempts.set(errorKey, attempts + 1);
        
        // Check if we should retry
        if (attempts < 3) {
            console.log(`Retrying request to ${url} (attempt ${attempts + 1})`);
            return;
        }
        
        // Open circuit breaker
        this.openCircuitBreaker(url);
        
        this.handleError(error, `Network Error (${url})`);
    }
    
    isCircuitOpen(url) {
        const circuitBreaker = this.circuitBreakers.get(url);
        if (!circuitBreaker) return false;
        
        // Auto-close after 30 seconds
        if (Date.now() - circuitBreaker.openedAt > 30000) {
            this.circuitBreakers.delete(url);
            return false;
        }
        
        return circuitBreaker.isOpen;
    }
    
    openCircuitBreaker(url) {
        this.circuitBreakers.set(url, {
            isOpen: true,
            openedAt: Date.now()
        });
    }
    
    resetCircuitBreaker(url) {
        this.circuitBreakers.delete(url);
        this.retryAttempts.delete(`network_${url}`);
    }
    
    updatePerformanceMetrics(responseTime) {
        const metrics = this.performanceMetrics;
        const totalResponseTime = metrics.averageResponseTime * (metrics.apiCalls - 1) + responseTime;
        metrics.averageResponseTime = totalResponseTime / metrics.apiCalls;
    }
    
    startPerformanceMonitoring() {
        // Monitor page performance
        if ('performance' in window) {
            window.addEventListener('load', () => {
                setTimeout(() => {
                    const perfData = performance.getEntriesByType('navigation')[0];
                    if (perfData) {
                        console.log('Page performance:', {
                            loadTime: perfData.loadEventEnd - perfData.loadEventStart,
                            domContentLoaded: perfData.domContentLoadedEventEnd - perfData.domContentLoadedEventStart,
                            firstPaint: perfData.responseStart - perfData.requestStart
                        });
                    }
                }, 0);
            });
        }
        
        // Monitor memory usage
        if ('memory' in performance) {
            setInterval(() => {
                const memory = performance.memory;
                const usedMB = memory.usedJSHeapSize / 1024 / 1024;
                const limitMB = memory.jsHeapSizeLimit / 1024 / 1024;
                
                if (usedMB > limitMB * 0.8) {
                    console.warn(`High memory usage: ${usedMB.toFixed(2)}MB / ${limitMB.toFixed(2)}MB`);
                    this.handleMemoryWarning(usedMB, limitMB);
                }
            }, 30000); // Check every 30 seconds
        }
    }
    
    handleMemoryWarning(usedMB, limitMB) {
        // Clear old errors to free memory
        if (this.errors.length > 20) {
            this.errors = this.errors.slice(-20);
        }
        
        // Trigger garbage collection if available
        if (window.gc) {
            window.gc();
        }
        
        this.showUserFriendlyMessage({
            message: 'Application is using high memory. Some features may be slowed down.',
            type: 'warning'
        });
    }
    
    showUserFriendlyMessage(error) {
        // Don't show too many messages
        const lastMessage = localStorage.getItem('lastErrorMessage');
        const now = Date.now();
        
        if (lastMessage && now - parseInt(lastMessage) < 5000) {
            return;
        }
        
        localStorage.setItem('lastErrorMessage', now.toString());
        
        // Create toast notification
        const toast = document.createElement('div');
        toast.className = 'error-toast';
        toast.innerHTML = `
            <div class="error-toast-content">
                <span class="error-toast-icon">⚠️</span>
                <span class="error-toast-message">
                    ${this.getUserFriendlyMessage(error)}
                </span>
                <button class="error-toast-close" onclick="this.parentElement.parentElement.remove()">×</button>
            </div>
        `;
        
        // Add styles if not already added
        if (!document.querySelector('#error-toast-styles')) {
            const style = document.createElement('style');
            style.id = 'error-toast-styles';
            style.textContent = `
                .error-toast {
                    position: fixed;
                    bottom: 20px;
                    right: 20px;
                    background: #f8d7da;
                    color: #721c24;
                    padding: 15px;
                    border-radius: 8px;
                    border: 1px solid #f5c6cb;
                    z-index: 10000;
                    max-width: 400px;
                    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                }
                .error-toast-content {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                }
                .error-toast-icon {
                    font-size: 18px;
                }
                .error-toast-message {
                    flex: 1;
                    font-size: 14px;
                }
                .error-toast-close {
                    background: none;
                    border: none;
                    font-size: 18px;
                    cursor: pointer;
                    color: #721c24;
                }
            `;
            document.head.appendChild(style);
        }
        
        document.body.appendChild(toast);
        
        // Auto-remove after 5 seconds
        setTimeout(() => {
            if (toast.parentElement) {
                toast.remove();
            }
        }, 5000);
    }
    
    getUserFriendlyMessage(error) {
        const message = error.message || error.toString();
        
        // Common error patterns and user-friendly messages
        if (message.includes('network') || message.includes('fetch')) {
            return 'Network connection issue. Please check your internet connection.';
        }
        
        if (message.includes('timeout')) {
            return 'Request timed out. Please try again.';
        }
        
        if (message.includes('permission') || message.includes('unauthorized')) {
            return 'Permission denied. Please check your access rights.';
        }
        
        if (message.includes('not found') || message.includes('404')) {
            return 'Requested resource not found.';
        }
        
        return 'An unexpected error occurred. Please try again.';
    }
    
    reportError(errorInfo) {
        // In production, send to monitoring service
        try {
            // Example: Send to error tracking service
            // fetch('/api/errors', {
            //     method: 'POST',
            //     headers: { 'Content-Type': 'application/json' },
            //     body: JSON.stringify(errorInfo)
            // });
        } catch (e) {
            console.error('Failed to report error:', e);
        }
    }
    
    // Public API
    getErrorSummary() {
        return {
            totalErrors: this.errors.length,
            recentErrors: this.errors.slice(-10),
            performanceMetrics: this.performanceMetrics,
            circuitBreakers: Array.from(this.circuitBreakers.keys())
        };
    }
    
    clearErrors() {
        this.errors = [];
        this.performanceMetrics.errors = 0;
    }
    
    // Safe wrapper for async operations
    async safeAsync(fn, fallback = null) {
        try {
            return await fn();
        } catch (error) {
            this.handleError(error, 'Safe Async Wrapper');
            return fallback;
        }
    }
    
    // Safe wrapper for DOM operations
    safeDOM(fn) {
        try {
            return fn();
        } catch (error) {
            this.handleError(error, 'Safe DOM Operation');
            return null;
        }
    }
}

// Initialize the error prevention system
window.errorPrevention = new ErrorPreventionSystem();

// Export for use in other scripts
if (typeof module !== 'undefined' && module.exports) {
    module.exports = ErrorPreventionSystem;
}
