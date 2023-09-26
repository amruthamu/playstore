import com.google.gson.annotations.SerializedName

data class DataResponse(
    val data: Data,
    val status: Boolean
)

data class Data(
    @SerializedName("Profile")
    val profiles: List<Profile>
)

data class Profile(
    val username: String,
    val name: String,
    @SerializedName("gwpAmount")
    val gwpAmount: String,
    @SerializedName("gwpTarget")
    val gwpTarget: String,
    @SerializedName("gwpPercent")
    val gwpPercent: String,
    @SerializedName("nopValue")
    val nopValue: String,
    val currency: String,
    @SerializedName("nopTarget")
    val nopTarget: String,
    @SerializedName("nopPercent")
    val nopPercent: String,
    val agentCount: String,
    val dealersCount: String,
    val today: String,
    val week: String
)
