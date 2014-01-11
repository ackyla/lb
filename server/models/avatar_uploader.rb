class AvatarUploader < CarrierWave::Uploader::Base
  def store_dir
    "avatars/#{model.class.to_s.underscore}/#{mounted_as}/#{model.id}"
  end

  def extension_white_list
    %w(jpg jpeg)
  end

  def filename
    "#{secure_token(10)}.#{file.extension}" if original_filename.present?
  end

  protected
  def secure_token(length=16)
    var = :"@#{mounted_as}_secure_token"
    model.instance_variable_get(var) or model.instance_variable_set(var, SecureRandom.hex(length/2))
  end

  def default_url
    "/avatars/default_avatar.jpg"
  end
end
