package davidakht.githubdao.data

data class UserDetailResponse(
    val avatar_url: String,
    val name: String,
    val login: String,
    val location: String,
    val company: String,
    val public_repos: String,
    val following: Int,
    val followers: Int,
    val id: String
)
