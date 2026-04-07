namespace PvzLauncher.Android
{
    public partial class MainPage : ContentPage
    {

        public MainPage()
        {
            InitializeComponent();
        }

        private async void Button_Clicked(object sender, EventArgs e)
        {
            await DisplayAlertAsync("TITLE", "I Want fuck you!", "YES", "NOT NO");
        }
    }
}
