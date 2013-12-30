class CreateNotifications < ActiveRecord::Migration
  def self.up
    create_table :notifications do |t|
      t.references :user
      t.string :notification_type
      t.references :location
      t.references :territory
      t.boolean :read, :default => false
      t.timestamps
    end
  end

  def self.down
    drop_table :notifications
  end
end
