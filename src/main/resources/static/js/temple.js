/**
 * Temple Puja Reminder System - Main JavaScript
 * Handles UI interactions and WhatsApp integration
 */

document.addEventListener('DOMContentLoaded', function () {

    // ── Auto-dismiss flash messages ──
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            const bsAlert = bootstrap.Alert.getInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            } else {
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 300);
            }
        }, 5000);
    });

    // ── Mobile number formatter ──
    const mobileInput = document.getElementById('mobileNumber');
    if (mobileInput) {
        mobileInput.addEventListener('input', function () {
            // Allow only digits
            this.value = this.value.replace(/\D/g, '').substring(0, 10);
        });

        mobileInput.addEventListener('blur', function () {
            const val = this.value.trim();
            if (val.length > 0 && val.length !== 10) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    }

    // ── Week number range indicator ──
    const weekInput = document.getElementById('weekNumber');
    if (weekInput) {
        weekInput.addEventListener('input', function () {
            const val = parseInt(this.value);
            if (val < 1 || val > 52) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    }

    // ── Tooltip initialization ──
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltipTriggerList.forEach(function (tooltipTriggerEl) {
        new bootstrap.Tooltip(tooltipTriggerEl, {
            trigger: 'hover'
        });
    });

    // ── Confirm delete with data attributes ──
    // (handled inline per-page for flexibility)

    // ── Active sidebar link highlight ──
    highlightActiveSidebar();

    // ── Search form: clear on empty ──
    const searchForm = document.querySelector('form[action*="/couples"]');
    if (searchForm) {
        const searchInput = searchForm.querySelector('input[name="search"]');
        if (searchInput && searchInput.value.trim() === '') {
            const clearBtn = searchForm.querySelector('.btn-outline-secondary');
            if (clearBtn) clearBtn.style.display = 'none';
        }
    }
});

/**
 * Highlights the currently active sidebar link based on URL path.
 */
function highlightActiveSidebar() {
    const currentPath = window.location.pathname;
    const sidebarLinks = document.querySelectorAll('.sidebar-menu .nav-link');

    sidebarLinks.forEach(function (link) {
        const href = link.getAttribute('href');
        if (href && href !== '/' && currentPath.startsWith(href)) {
            link.classList.add('active');
        } else if (href === '/' && currentPath === '/') {
            link.classList.add('active');
        }
    });
}

/**
 * Sends a WhatsApp reminder for a specific couple by ID.
 * Fetches the generated URL from the backend and opens WhatsApp.
 *
 * @param {number} coupleId - The ID of the couple
 */
function sendWhatsApp(coupleId) {
    const btn = event.currentTarget;
    const originalText = btn.innerHTML;

    // Show loading state
    btn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Opening WhatsApp...';
    btn.disabled = true;

    fetch('/reminders/whatsapp/' + coupleId)
        .then(function (response) {
            if (!response.ok) {
                throw new Error('Failed to generate WhatsApp URL');
            }
            return response.text();
        })
        .then(function (url) {
            window.open(url, '_blank');
            btn.innerHTML = '<i class="bi bi-check-circle-fill me-2"></i>Opened!';
            btn.classList.remove('btn-whatsapp');
            btn.classList.add('btn-success');

            setTimeout(function () {
                btn.innerHTML = originalText;
                btn.disabled = false;
                btn.classList.add('btn-whatsapp');
                btn.classList.remove('btn-success');
            }, 3000);
        })
        .catch(function (error) {
            console.error('WhatsApp error:', error);
            btn.innerHTML = '<i class="bi bi-exclamation-triangle-fill me-2"></i>Error!';
            btn.classList.add('btn-danger');
            btn.classList.remove('btn-whatsapp');

            setTimeout(function () {
                btn.innerHTML = originalText;
                btn.disabled = false;
                btn.classList.remove('btn-danger');
                btn.classList.add('btn-whatsapp');
            }, 3000);

            showToast('Error generating WhatsApp link. Please try again.', 'danger');
        });
}

/**
 * Shows a toast notification.
 *
 * @param {string} message - The message to display
 * @param {string} type - Bootstrap color type (success, danger, warning, info)
 */
function showToast(message, type) {
    type = type || 'info';
    const toastContainer = getOrCreateToastContainer();

    const toastEl = document.createElement('div');
    toastEl.className = 'toast align-items-center text-bg-' + type + ' border-0';
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;

    toastContainer.appendChild(toastEl);
    const toast = new bootstrap.Toast(toastEl, { delay: 4000 });
    toast.show();

    toastEl.addEventListener('hidden.bs.toast', function () {
        toastEl.remove();
    });
}

/**
 * Gets or creates the toast container in the DOM.
 */
function getOrCreateToastContainer() {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
        container.style.zIndex = '9999';
        document.body.appendChild(container);
    }
    return container;
}

/**
 * Inserts a placeholder text at the cursor position in a textarea.
 *
 * @param {string} placeholder - The placeholder text to insert
 */
function insertPlaceholder(placeholder) {
    const textarea = document.getElementById('messageContent');
    if (!textarea) return;

    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const text = textarea.value;

    textarea.value = text.substring(0, start) + placeholder + text.substring(end);
    textarea.selectionStart = textarea.selectionEnd = start + placeholder.length;
    textarea.focus();
}

/**
 * Confirms and processes delete actions via modal.
 *
 * @param {HTMLElement} btn - The button element with data attributes
 */
function confirmDelete(btn) {
    const id = btn.getAttribute('data-id');
    const name = btn.getAttribute('data-name');
    const deleteModal = document.getElementById('deleteModal');
    if (!deleteModal) return;

    document.getElementById('deleteCoupleName').textContent = name;
    document.getElementById('deleteForm').action = '/couples/delete/' + id;
    new bootstrap.Modal(deleteModal).show();
}
