package br.com.fiap.gitdash.github;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Controller
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/")
    public String getUserInfo(Model model,
                              @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient,
                              @AuthenticationPrincipal OAuth2User principal) {

        // Obtém o token OAuth2
        String tokenValue = authorizedClient.getAccessToken().getTokenValue();

        // Usa o serviço GitHub para buscar os repositórios do usuário autenticado
        List<RepositoryInfo> repos = gitHubService.getUserRepositories(tokenValue);

        // Obtém informações do usuário do GitHub a partir do token
        if (principal != null) {
            String name = principal.getAttribute("name");
            String avatarUrl = principal.getAttribute("avatar_url");
            String githubProfileUrl = principal.getAttribute("html_url");

            model.addAttribute("name", name);
            model.addAttribute("avatarUrl", avatarUrl);
            model.addAttribute("githubProfileUrl", githubProfileUrl);
        }

        // Adiciona a lista de repositórios ao modelo
        model.addAttribute("repos", repos);

        // Retorna a view 'user.html'
        return "user";
    }
}
