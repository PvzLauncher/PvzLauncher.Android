namespace PvzLauncher.Android.Controls;

public partial class GameDetail : ContentView
{
	//ImageSource image,String title,string notes,ContentPage targetpage
	public required ImageSource Icon { get; set; }
	public required string Title { get; set; }
	public required string Notes { get; set; }
	public required string TargetPage {  get; set; }

    public GameDetail()
	{
		InitializeComponent();
		Loaded += (s, e) =>
		{
            GameImage.Source = Icon;
            GameNote.Text = Notes;
            GameTitle.Text = Title;
        };

	}

    private void InfoButton_Clicked(object sender, EventArgs e)
    {
		Shell.Current.GoToAsync(TargetPage);
    }
}