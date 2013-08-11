require 'securerandom'

class User < ActiveRecord::Base
  def generate_token
    token = SecureRandom.base64(16)
    self.token = token
  end

  belongs_to :room
  before_create :generate_token
end
