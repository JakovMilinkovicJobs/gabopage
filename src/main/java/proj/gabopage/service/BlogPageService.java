package proj.gabopage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import proj.gabopage.model.BlogPage;
import proj.gabopage.repository.BlogPageRepository;

import java.io.IOException;

@Service
public class BlogPageService {

    private static final long MAIN_PAGE_ID = 1L;
    private static final String DEFAULT_HTML = "<p>Welcome to the blog.</p>";

    private final BlogPageRepository repo;

    public BlogPageService(BlogPageRepository repo) {
        this.repo = repo;
    }

    public BlogPage getOrCreateMainPage() {
        return repo.findById(MAIN_PAGE_ID).orElseGet(() -> {
            BlogPage page = new BlogPage();
            page.setId(MAIN_PAGE_ID);
            page.setRichHtml(DEFAULT_HTML);
            return repo.save(page);
        });
    }

    public BlogPage saveMainPage(String richHtml,
                                 MultipartFile profileImage,
                                 boolean removeProfileImage) throws IOException {
        BlogPage page = repo.findById(MAIN_PAGE_ID).orElseGet(BlogPage::new);
        page.setId(MAIN_PAGE_ID);
        page.setRichHtml(richHtml);

        if (removeProfileImage) {
            page.setProfileImage(null);
            page.setProfileImageContentType(null);
        } else if (profileImage != null && !profileImage.isEmpty()) {
            page.setProfileImage(profileImage.getBytes());
            page.setProfileImageContentType(profileImage.getContentType());
        }

        return repo.save(page);
    }
}
