class Notification < ActiveRecord::Base
  belongs_to :user
  belongs_to :detection
  scope :unread, conditions: {read: false}
end
