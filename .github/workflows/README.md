# GitHub Actions Workflows

This directory contains automated workflows for the LMT (Links Management Tool) project using GitHub Actions.

## 📋 Available Workflows

### 1. **Automated Tasks** (`automated-tasks.yml`)

A comprehensive workflow that runs multiple automated jobs on different schedules:

#### 🕐 Schedule:
- **Daily Backup**: 2:00 AM UTC
- **Health Check**: Every 6 hours
- **Weekly Cleanup**: Sundays at 3:00 AM UTC
- **Monthly Deployment Check**: 1st of each month at 4:00 AM UTC

#### 🔄 Jobs:

##### 📦 **Backup Job**
- **Frequency**: Daily at 2:00 AM UTC
- **Purpose**: Create automated backups of all important files
- **What it backs up**:
  - All HTML files (`*.html`)
  - CSS files (`*.css`)
  - JavaScript files (`*.js`)
  - Configuration files (`*.yml`, `*.yaml`, `*.toml`, `*.sh`)
  - Documentation (`*.md`)
- **Features**:
  - Creates backup manifest with metadata
  - Compresses backups for storage efficiency
  - Retains backups for 30 days
  - Uploads as GitHub artifacts

##### 🏥 **Health Check Job**
- **Frequency**: Every 6 hours
- **Purpose**: Monitor system health and functionality
- **What it checks**:
  - Backend API connectivity (`/auth/login`, `/links/search`)
  - Frontend file integrity
  - LMT branding consistency
  - HTML structure validation
- **Features**:
  - Generates health reports
  - Tests live endpoints
  - Validates file structure

##### 🧹 **Weekly Cleanup Job**
- **Frequency**: Sundays at 3:00 AM UTC
- **Purpose**: Maintain repository hygiene
- **What it cleans**:
  - Old GitHub artifacts (>7 days)
  - Temporary files (`*.tmp`, `*.log`)
  - System files (`.DS_Store`)
  - Old backup directories (>30 days)
- **Features**:
  - Automated artifact cleanup
  - Repository size optimization

##### 🚀 **Deployment Check Job**
- **Frequency**: Monthly on 1st at 4:00 AM UTC
- **Purpose**: Verify deployment configurations
- **What it checks**:
  - Render configuration (`render.yaml`)
  - Fly.io configuration (`fly.toml`)
  - Deployment scripts (`deploy.sh`)
  - API endpoint accessibility
  - LMT branding consistency
- **Features**:
  - Configuration validation
  - Live deployment testing
  - Branding verification

### 2. **Code Quality & Testing** (`code-quality.yml`)

Automated code quality checks and testing:

#### 🕐 Schedule:
- **Automated**: Weekdays at 8:00 AM UTC
- **Event-driven**: On pushes to main/develop branches
- **Event-driven**: On pull requests to main

#### 🔍 Jobs:

##### 📝 **HTML Validation**
- **Purpose**: Validate HTML structure and standards
- **Checks**:
  - DOCTYPE declaration
  - Proper HTML structure
  - Required meta tags (charset, viewport)
  - LMT branding presence
  - Broken link detection
- **Features**:
  - Comprehensive HTML validation
  - Branding consistency checks

##### 🔍 **JavaScript Linting**
- **Purpose**: Ensure code quality and standards
- **Tools**: ESLint with recommended rules
- **Checks**:
  - Syntax validation
  - Code style enforcement
  - Best practices compliance
- **Features**:
  - Extracts JS from HTML files
  - Generates detailed linting reports

##### 🔒 **Security Scan**
- **Purpose**: Identify potential security issues
- **Checks**:
  - Sensitive information detection
  - Unsafe inline scripts
  - External dependency analysis
  - API key/password exposure
- **Features**:
  - Automated security scanning
  - Risk assessment

##### 📊 **Performance Check**
- **Purpose**: Monitor performance metrics
- **Checks**:
  - File size analysis
  - Repository size monitoring
  - Image optimization
  - Large file detection
- **Features**:
  - Performance reporting
  - Size optimization recommendations

##### ♿ **Accessibility Check**
- **Purpose**: Ensure accessibility compliance
- **Checks**:
  - Image alt text
  - Form labels
  - Heading structure
  - Meta descriptions
  - Title tags
- **Features**:
  - WCAG compliance checks
  - Accessibility reporting

## 🚀 Manual Triggers

Both workflows support manual execution via `workflow_dispatch`:

### How to Run Manually:

1. Go to your repository on GitHub
2. Click on "Actions" tab
3. Select the workflow you want to run
4. Click "Run workflow"
5. Choose the specific task type:
   - **Automated Tasks**: backup, health-check, cleanup, deployment-check
   - **Code Quality**: Runs all quality checks

## 📊 Reports & Artifacts

### Available Reports:
- **Backup Reports**: Detailed backup summaries with file counts and sizes
- **Health Reports**: System health status with endpoint testing results
- **Deployment Reports**: Configuration and deployment status
- **Code Quality Reports**: Comprehensive analysis of code quality

### Artifacts:
- **Backups**: Compressed backup files (retained 30 days)
- **Health Reports**: JSON health check results
- **Deployment Reports**: JSON deployment analysis
- **Validation Reports**: Detailed validation results

## 🔧 Configuration

### Environment Variables:
- `NODE_VERSION`: '18' (Node.js version for workflows)
- `BACKUP_RETENTION_DAYS`: 30 (Backup retention period)

### Customization:
You can modify the schedules by updating the cron expressions:
- `0 2 * * *` - Daily at 2:00 AM UTC
- `0 */6 * * *` - Every 6 hours
- `0 3 * * 0` - Weekly on Sunday at 3:00 AM UTC
- `0 4 1 * *` - Monthly on 1st at 4:00 AM UTC
- `0 8 * * 1-5` - Weekdays at 8:00 AM UTC

## 📈 Benefits

### Automation Benefits:
- **Continuous Monitoring**: Health checks every 6 hours
- **Data Protection**: Daily automated backups
- **Maintenance**: Weekly cleanup to keep repository optimized
- **Quality Assurance**: Regular code quality checks
- **Security**: Automated security scanning

### Operational Benefits:
- **Early Detection**: Issues caught early through automated checks
- **Consistency**: Standardized quality checks
- **Documentation**: Automated reports for audit trails
- **Reliability**: Reduced manual intervention
- **Scalability**: Automated processes that grow with your project

## 🛠️ Troubleshooting

### Common Issues:
1. **Workflow Failures**: Check the Actions tab for detailed error logs
2. **Backup Failures**: Verify file permissions and disk space
3. **Health Check Failures**: Check backend service status
4. **Timeout Issues**: Increase timeout values in workflow steps

### Getting Help:
- Check GitHub Actions logs for detailed error messages
- Review workflow run summaries for quick diagnostics
- Monitor artifact storage usage
- Check cron schedule timing

## 🔄 Maintenance

### Regular Tasks:
- Monitor workflow success rates
- Review backup retention policies
- Update security scanning rules
- Optimize performance thresholds
- Update cron schedules as needed

### Updates:
- Review and update Node.js versions
- Update action versions in workflows
- Modify check criteria based on project needs
- Add new quality checks as required

---

**Note**: These workflows are designed to run automatically but can also be triggered manually as needed. All reports and artifacts are available for download from the GitHub Actions interface.
